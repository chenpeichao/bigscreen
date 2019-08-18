package com.hubpd.bigscreen.mapper.weishu_pdmi;

import com.github.pagehelper.Page;
import com.hubpd.bigscreen.bean.weishu_pdmi.Article;
import com.hubpd.bigscreen.bean.weishu_pdmi.ArticleWithBLOBs;
import com.hubpd.bigscreen.dto.PubArticleDTO;

import java.util.Map;

public interface ArticleMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ArticleWithBLOBs record);

    int insertSelective(ArticleWithBLOBs record);

    ArticleWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ArticleWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ArticleWithBLOBs record);

    int updateByPrimaryKey(Article record);

    /**
     * 获取公众号集合的微信文章列表
     *
     * @param pubAccountIdList 公众号id集合
     * @param headType         头条类型(0:全部；1:头条；2:非头条)
     * @param startPublishTime 文章发布起始时间(yyyy-MM-dd HH:mm:ss)
     * @param endPublishTime   文章发布截止时间(yyyy-MM-dd HH:mm:ss)
     * @param sortName         排序字段
     * @param sortBy           升序/降序
     * @return
     */
    public Page<PubArticleDTO> getPubArticlelist(Map<String, Object> paramMap);
}