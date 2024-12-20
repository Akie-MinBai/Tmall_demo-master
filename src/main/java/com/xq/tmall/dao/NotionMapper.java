package com.xq.tmall.dao;

import com.xq.tmall.entity.Notion;
import com.xq.tmall.util.PageUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotionMapper {
    Integer insertOne(@Param("notion") Notion notion);
    Integer updateOne(@Param("notion") Notion notion);

    List<Notion> select(@Param("notion_name") String notion_name, @Param("pageUtil") PageUtil pageUtil);
    Notion selectOne(@Param("notion_id") Integer notion_id);
    Integer selectTotal(@Param("notion_name") String notion_name);
}