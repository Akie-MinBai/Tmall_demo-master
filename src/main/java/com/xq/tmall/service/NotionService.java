package com.xq.tmall.service;

import com.xq.tmall.entity.Notion;
import com.xq.tmall.util.PageUtil;

import java.util.List;

public interface NotionService {
    boolean add(Notion notion);
    boolean update(Notion notion);

    List<Notion> getList(String notion_name, PageUtil pageUtil);
    Notion get(Integer notion_id);
    Integer getTotal(String notion_name);
}
