<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.sql.*" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
            <html>
                <%!
                public static final String DBDRIVER="com.mysql.cj.jdbc.Driver";
                public static final String DBURL="jdbc:mysql://localhost:3306/tmalldemodb?&useSSL=false&serverTimezone=UTC";
                public static final String DBUSER="root";
                public static final String DBPASS="123456";
            %>
            
                <%
                try{
                    Connection conn=null;
                    PreparedStatement pst=null;
                    int rs=0;
                String notion_id=request.getParameter("notion_id");
                  Class.forName(DBDRIVER);
                  conn=DriverManager.getConnection(DBURL,DBUSER,DBPASS);
                  String sql_delete="delete from notion where notion_id="+17+"";
                  pst=conn.prepareStatement(sql_delete);
                  rs=pst.executeUpdate();
                } catch(Exception e){
                    
                }
                      
            %>

                <head>
                    <script>
                        $(function () {
                            console.log($("#details_notion_id").val());

                            if ($("#details_notion_id").val() === "") {
                                //单击保存按钮时
                                $("#btn_notion_save").click(function () {
                                    var notion_name = $.trim($("#input_notion_name").val());
                                    var notion_image_src = $.trim($("#pic_notion").attr("src"));
                                    //校验数据合法性
                                    var yn = true;
                                    if (notion_name === "") {
                                        styleUtil.basicErrorShow($("#lbl_notion_name"));
                                        yn = false;
                                    }
                                    if (!yn) {
                                        return;
                                    }

                                    var dataList = {
                                        "notion_name": notion_name,
                                        "notion_image_src": notion_image_src
                                    };
                                    doAction(dataList, "admin/notion", "POST");
                                });
                            } else {
                                //设置分类编号
                                $("#span_notion_id").text('${requestScope.notion.notion_id}');
                                //判断文件是否允许上传
                                if ($("#pic_notion").attr("src") === undefined) {
                                    $(".details_picList_fileUpload").css("display", "inline-block");
                                } else {
                                    $(".details_picList_fileUpload").css("display", "none");
                                }
                                //单击保存按钮时
                                $("#btn_notion_save").click(function () {
                                    var notion_id = $("#details_notion_id").val();
                                    var notion_name = $.trim($("#input_notion_name").val());
                                    var notion_image_src = $.trim($("#pic_notion").attr("src"));
                                    if (notion_name === "") {
                                        styleUtil.basicErrorShow($("#lbl_notion_name"));
                                        yn = false;
                                    }
                                    if (!yn) {
                                        return;
                                    }

                                    var dataList = {
                                        "notion_name": notion_name,
                                        "notion_image_src": notion_image_src
                                    };
                                    doAction(dataList, "admin/notion/" + notion_id, "PUT");
                                });
                                //单击删除按钮时
                                $("#btn_notion_delete").click(function () {
                                    console.log(2222);
                                    var notion_id = $("#details_notion_id").val();
                                });
                            }

                            //单击图片列表项时
                            $(".details_picList").on("click", "li:not(.details_picList_fileUpload)", function () {
                                var img = $(this);
                                var fileUploadInput = $(this).parents("ul").children(".details_picList_fileUpload");
                                $("#btn-ok").unbind("click").click(function () {
                                    img.remove();
                                    fileUploadInput.css("display", "inline-block");
                                    $('#modalDiv').modal("hide");
                                });
                                $(".modal-body").text("您确定要删除该分类图片吗？");
                                $('#modalDiv').modal();
                            });
                            //单击取消按钮时
                            $("#btn_notion_cancel").click(function () {
                                $(".menu_li[data-toggle=notion]").click();
                            });
                            //获取到输入框焦点时
                            $("input:text").focus(function () {
                                styleUtil.basicErrorHide($(this).prev("label"));
                            });
                        });

                        //图片上传
                        function uploadImage(fileDom) {
                            //获取文件
                            var file = fileDom.files[0];
                            //判断类型
                            var imageType = /^image\//;
                            if (file === undefined || !imageType.test(file.type)) {
                                $("#btn-ok").unbind("click").click(function () {
                                    $("#modalDiv").modal("hide");
                                });
                                $(".modal-body").text("请选择图片！");
                                $('#modalDiv').modal();
                                return;
                            }
                            //判断大小
                            if (file.size > 3192000) {
                                $("#btn-ok").unbind("click").click(function () {
                                    $("#modalDiv").modal("hide");
                                });
                                $(".modal-body").text("图片大小不能超过3M！");
                                $('#modalDiv').modal();
                                return;
                            }
                            //清空值
                            $(fileDom).val('');
                            var formData = new FormData();
                            formData.append("file", file);
                            //上传图片
                            $.ajax({
                                url: "/tmall/admin/uploadNotionImage",
                                type: "post",
                                data: formData,
                                contentType: false,
                                processData: false,
                                dataType: "json",
                                mimeType: "multipart/form-data",
                                success: function (data) {
                                    $(fileDom).attr("disabled", false).prev("span").text("上传图片");
                                    if (data.success) {
                                        $(fileDom).parent('.details_picList_fileUpload').before("<li><img src='${pageContext.request.contextPath}/res/images/item/notionPicture/" + data.fileName + "' id='pic_notion'  width='1190px' height='150px'/></li>").css("display", "none");
                                    } else {
                                        alert("图片上传异常！");
                                    }
                                },
                                beforeSend: function () {
                                    $(fileDom).attr("disabled", true).prev("span").text("图片上传中...");
                                },
                                error: function () {

                                }
                            });
                        }

                        //分类操作
                        function doAction(dataList, url, type) {
                            $.ajax({
                                url: url,
                                type: type,
                                data: dataList,
                                traditional: true,
                                success: function (data) {
                                    $("#btn_notion_save").attr("disabled", false).val("保存");
                                    if (data.success) {
                                        $("#btn-ok,#btn-close").unbind("click").click(function () {
                                            $('#modalDiv').modal("hide");
                                            setTimeout(function () {
                                                //ajax请求页面
                                                ajaxUtil.getPage("notion/" + data.notion_id, null, true);
                                            }, 170);
                                        });
                                        $(".modal-body").text("保存成功！");
                                        $('#modalDiv').modal();
                                    }
                                },
                                beforeSend: function () {
                                    $("#btn_product_save").attr("disabled", true).val("保存中...");
                                },
                                error: function () {

                                }
                            });

                        }

                    </script>
                    <style rel="stylesheet">
                        .details_property_list {}

                        .details_property_list>li {
                            list-style: none;
                            padding: 5px 0;
                        }

                        div.br {
                            height: 20px;
                        }
                    </style>
                </head>

                <body>

                    <div class="details_div_first">
                        <input type="hidden" value="${requestScope.notion.notion_id}" id="details_notion_id" />
                        <div class="frm_div">
                            <label class="frm_label text_info" id="lbl_notion_id">公告编号</label>
                            <span class="details_value" id="span_notion_id">系统指定</span>
                        </div>
                        <div class="frm_div">
                            <label class="frm_label text_info" id="lbl_notion_name" for="input_notion_name">公告名称</label>
                            <input class="frm_input" id="input_notion_name" type="text" maxlength="50"
                                value="${requestScope.notion.notion_name}" />
                        </div>
                    </div>
                    <div class="details_div">
                        <span class="frm_error_msg" id="text_notion_image_details_msg"></span>
                    </div>
                    <div class="details_div details_div_last">
                    </div>
                    <div class="details_tools_div">
                        <input class="frm_btn" id="btn_notion_save" type="button" value="保存" />
                        <input class="frm_btn frm_clear" id="btn_notion_cancel" type="button" value="取消" />
                        <form name="form_notion" method="POST">
                        <input class="frm_btn frm_clear" id="btn_notion_delete" type="button" value="删除" />
                    </form>
                    </div>

                    <%-- 模态框 --%>
                        <div class="modal fade" id="modalDiv" tabindex="-1" role="dialog" aria-labelledby="modalDiv"
                            aria-hidden="true" data-backdrop="static">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h4 class="modal-title" id="myModalLabel">提示</h4>
                                    </div>
                                    <div class="modal-body">您确定要删除分类图片吗？</div>
                                    <div class="modal-footer">
                                        <button type="submit" class="btn btn-primary" id="btn-ok">确定</button>
                                        <button type="button" class="btn btn-default" data-dismiss="modal"
                                            id="btn-close">关闭</button>
                                    </div>
                                </div>
                                <%-- /.modal-content --%>
                            </div>
                            <%-- /.modal --%>
                        </div>
                        <div class="loader"></div>
                </body>

            </html>