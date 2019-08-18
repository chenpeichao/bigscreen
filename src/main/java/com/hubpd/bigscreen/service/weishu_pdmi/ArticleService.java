package com.hubpd.bigscreen.service.weishu_pdmi;

import com.github.pagehelper.Page;
import com.hubpd.bigscreen.dto.PubArticleDTO;

import java.util.List;

/**
 * 微信文章service
 *
 * @author ceek
 * @create 2019-08-18 15:30
 **/
public interface ArticleService {
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
    public Page<PubArticleDTO> getPubArticlelist(List<Integer> pubAccountIdList, Integer headType, String startPublishTime, String endPublishTime, String sortName, String sortBy);
}
