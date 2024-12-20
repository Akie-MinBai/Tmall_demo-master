package com.xq.tmall.entity;

import java.util.List;

/**
 * 类型实体类
 * @author 贤趣项目小组
 */

public class Notion {
    private Integer notion_id/*类型ID*/;

    private String notion_name/*类型名称*/;

    private String notion_image_src/*类型图片路径*/;

    private List<Property> propertyList/*属性列表*/;

    private List<Product> productList/*产品集合*/;

    private List<List<Product>> complexProductList/*产品二维集合*/;

    @Override
    public String toString() {
        return "Notion{" +
                "notion_id=" + notion_id +
                ", notion_name='" + notion_name + '\'' +
                ", notion_image_src='" + notion_image_src + '\'' +
                '}';
    }

    public Notion(){

    }

    public Notion(Integer notion_id, String notion_name, String notion_image_src) {
        this.notion_id = notion_id;
        this.notion_name = notion_name;
        this.notion_image_src = notion_image_src;
    }

    public Integer getnotion_id() {
        return notion_id;
    }

    public Notion setnotion_id(Integer notion_id) {
        this.notion_id = notion_id;
        return this;
    }

    public String getnotion_name() {
        return notion_name;
    }

    public Notion setnotion_name(String notion_name) {
        this.notion_name = notion_name;
        return this;
    }

    public String getnotion_image_src() {
        return notion_image_src;
    }

    public Notion setnotion_image_src(String notion_image_src) {
        this.notion_image_src = notion_image_src;
        return this;
    }

    public List<List<Product>> getComplexProductList() {
        return complexProductList;
    }

    public Notion setComplexProductList(List<List<Product>> productList) {
        this.complexProductList = productList;
        return this;
    }

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public Notion setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
        return this;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public Notion setProductList(List<Product> productList) {
        this.productList = productList;
        return this;
    }
}
