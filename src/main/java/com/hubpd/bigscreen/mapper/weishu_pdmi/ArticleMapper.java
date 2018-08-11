package com.hubpd.bigscreen.mapper.weishu_pdmi;

import com.hubpd.bigscreen.bean.weishu_pdmi.Article;
import com.hubpd.bigscreen.bean.weishu_pdmi.ArticleWithBLOBs;

public interface ArticleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ArticleWithBLOBs record);

    int insertSelective(ArticleWithBLOBs record);

    ArticleWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ArticleWithBLOBs record);

    int updateByPrimaryKey(Article record);
}