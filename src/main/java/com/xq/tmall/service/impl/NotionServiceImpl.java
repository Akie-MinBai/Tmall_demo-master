package com.xq.tmall.service.impl;

import com.xq.tmall.dao.NotionMapper;
import com.xq.tmall.entity.Notion;
import com.xq.tmall.service.NotionService;
import com.xq.tmall.util.PageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("notionService")
public class NotionServiceImpl implements NotionService {

    private NotionMapper notionMapper;

    @Resource(name = "notionMapper")
    public void setNotionMapper(NotionMapper notionMapper) {
        this.notionMapper = notionMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean add(Notion notion) {
        return notionMapper.insertOne(notion)>0;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public boolean update(Notion notion) {
        return notionMapper.updateOne(notion)>0;
    }

    @Override
    public List<Notion> getList(String notion_name, PageUtil pageUtil) {
        return notionMapper.select(notion_name,pageUtil);
    }

    @Override
    public Notion get(Integer notion_id) {
        return notionMapper.selectOne(notion_id);
    }

    @Override
    public Integer getTotal(String notion_name) {
        return notionMapper.selectTotal(notion_name);
    }
}
