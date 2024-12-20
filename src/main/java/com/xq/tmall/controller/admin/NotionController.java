package com.xq.tmall.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xq.tmall.controller.BaseController;
import com.xq.tmall.entity.Notion;
import com.xq.tmall.entity.Property;
import com.xq.tmall.service.NotionService;
import com.xq.tmall.service.NotionService;
import com.xq.tmall.service.LastIDService;
import com.xq.tmall.service.PropertyService;
import com.xq.tmall.util.PageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 后台管理-产品类型页
 */
@Controller
public class NotionController extends BaseController {
    @Resource(name = "notionService")
    private NotionService notionService;
    @Resource(name = "lastIDService")
    private LastIDService lastIDService;
    @Resource(name = "propertyService")
    private PropertyService propertyService;

    //转到后台管理-产品类型页-ajax
    @RequestMapping(value = "admin/notion", method = RequestMethod.GET)
    public String goToPage(HttpSession session, Map<String, Object> map) {
        logger.info("获取前10条产品类型列表");
        PageUtil pageUtil = new PageUtil(0, 10);
        List<Notion> notionList = notionService.getList(null, pageUtil);
        map.put("notionList", notionList);
        logger.info("获取产品类型总数量");
        Integer notionCount = notionService.getTotal(null);
        map.put("notionCount", notionCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(notionCount);
        map.put("pageUtil", pageUtil);

        logger.info("转到后台管理-分类页-ajax方式");
        return "admin/notionManagePage";
    }

    //转到后台管理-产品类型详情页-ajax
    @RequestMapping(value = "admin/notion/{cid}", method = RequestMethod.GET)
    public String goToDetailsPage(HttpSession session, Map<String, Object> map, @PathVariable Integer cid/* 分类ID */) {
        logger.info("获取notion_id为{}的分类信息", cid);
        Notion notion = notionService.get(cid);
        logger.info("获取分类子信息-属性列表");
        notion.setPropertyList(propertyService.getList(new Property().setProperty_notion(notion), null));
        map.put("notion", notion);

        logger.info("转到后台管理-分类详情页-ajax方式");
        return "admin/include/notionDetails";
    }

    //转到后台管理-产品类型添加页-ajax
    @RequestMapping(value = "admin/notion/new", method = RequestMethod.GET)
    public String goToAddPage(HttpSession session, Map<String, Object> map) {
        logger.info("转到后台管理-分类添加页-ajax方式");
        return "admin/include/notionDetails";
    }
//    删除
    @RequestMapping(value = "deleteAll.jsp", method = RequestMethod.GET)
    public String deleteAll(HttpSession session, Map<String, Object> map,String name) {
        if(name != null){
            System.out.println(name);}
else{
            System.out.println(1);

        }
return "admin/notionManagePage";
    }

    //添加产品类型信息-ajax
    @ResponseBody
    @RequestMapping(value = "admin/notion", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String addnotion(@RequestParam String notion_name/* 分类名称 */,
                              @RequestParam String notion_image_src/* 分类图片路径 */) {
        JSONObject jsonObject = new JSONObject();
        logger.info("整合分类信息");
        Notion notion = new Notion()
                .setnotion_name(notion_name)
                .setnotion_image_src(notion_image_src.substring(notion_image_src.lastIndexOf("/") + 1));
        logger.info("添加分类信息");
        boolean yn = notionService.add(notion);
        if (yn) {
            int notion_id = lastIDService.selectLastID();
            logger.info("添加成功！,新增分类的ID值为：{}", notion_id);
            jsonObject.put("success", true);
            jsonObject.put("notion_id", notion_id);
        } else {
            jsonObject.put("success", false);
            logger.warn("添加失败！事务回滚");
            throw new RuntimeException();
        }

        return jsonObject.toJSONString();
    }

    //更新产品类型信息-ajax
    @ResponseBody
    @RequestMapping(value = "admin/notion/{notion_id}", method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
    public String updatenotion(@RequestParam String notion_name/* 分类名称 */,
                                 @RequestParam String notion_image_src/* 分类图片路径 */,
                                 @PathVariable("notion_id") Integer notion_id/* 分类ID */) {
        JSONObject jsonObject = new JSONObject();
        logger.info("整合分类信息");
        Notion notion = new Notion()
                .setnotion_id(notion_id)
                .setnotion_name(notion_name)
                .setnotion_image_src(notion_image_src.substring(notion_image_src.lastIndexOf("/") + 1));
        logger.info("更新分类信息，分类ID值为：{}", notion_id);
        boolean yn = notionService.update(notion);
        if (yn) {
            logger.info("更新成功！");
            jsonObject.put("success", true);
            jsonObject.put("notion_id", notion_id);
        } else {
            jsonObject.put("success", false);
            logger.info("更新失败！事务回滚");
            throw new RuntimeException();
        }

        return jsonObject.toJSONString();
    }

    //按条件查询产品类型-ajax
    @ResponseBody
    @RequestMapping(value = "admin/notion/{index}/{count}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public String getnotionBySearch(@RequestParam(required = false) String notion_name/* 分类名称 */,
                                      @PathVariable Integer index/* 页数 */,
                                      @PathVariable Integer count/* 行数 */) throws UnsupportedEncodingException {
        //移除不必要条件
        if (notion_name != null) {
            //如果为非空字符串则解决中文乱码：URLDecoder.decode(String,"UTF-8");
            notion_name = "".equals(notion_name) ? null : URLDecoder.decode(notion_name, "UTF-8");
        }

        JSONObject object = new JSONObject();
        logger.info("按条件获取第{}页的{}条分类", index + 1, count);
        PageUtil pageUtil = new PageUtil(index, count);
        List<Notion> notionList = notionService.getList(notion_name, pageUtil);
        object.put("notionList", JSONArray.parseArray(JSON.toJSONString(notionList)));
        logger.info("按条件获取分类总数量");
        Integer notionCount = notionService.getTotal(notion_name);
        object.put("notionCount", notionCount);
        logger.info("获取分页信息");
        pageUtil.setTotal(notionCount);
        object.put("totalPage", pageUtil.getTotalPage());
        object.put("pageUtil", pageUtil);

        return object.toJSONString();
    }

    // 上传产品类型图片-ajax
    @ResponseBody
    @RequestMapping(value = "admin/uploadnotionImage", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public String uploadnotionImage(@RequestParam MultipartFile file, HttpSession session) {
        String originalFileName = file.getOriginalFilename();
        logger.info("获取图片原始文件名:  {}", originalFileName);
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + extension;
        String filePath = session.getServletContext().getRealPath("/") + "res/images/item/notionPicture/" + fileName;

        logger.info("文件上传路径：{}", filePath);
        JSONObject object = new JSONObject();
        try {
            logger.info("文件上传中...");
            file.transferTo(new File(filePath));
            logger.info("文件上传完成");
            object.put("success", true);
            object.put("fileName", fileName);
        } catch (IOException e) {
            logger.warn("文件上传失败!");
            e.printStackTrace();
            object.put("success", false);
        }

        return object.toJSONString();
    }
}