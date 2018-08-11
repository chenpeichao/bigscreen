package com.hubpd.bigscreen.mapper.weishu_pdmi;

import com.hubpd.bigscreen.bean.weishu_pdmi.ArticleStat;
import com.hubpd.bigscreen.vo.WXArticleAnalyseVO;
import com.hubpd.bigscreen.vo.WXUserAnalyseVO;

import java.util.List;
import java.util.Map;

public interface ArticleStatMapper {
    int insert(ArticleStat record);

    int insertSelective(ArticleStat record);

    /**
     *  根据公众号id列表，查询文章信息以及指定日期段的阅读数和点赞数
     * @param params            公众号id列表,开始时间,结束时间
     * @return
     */
    public List<WXArticleAnalyseVO> findgetWXArticleStatByAccountIdListAndSearchDate(Map<String, Object> params);

}