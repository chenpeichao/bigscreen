package com.hubpd.bigscreen.service.weishu_pdmi.impl;

import com.github.pagehelper.Page;
import com.hubpd.bigscreen.dto.PubArticleDTO;
import com.hubpd.bigscreen.mapper.weishu_pdmi.ArticleMapper;
import com.hubpd.bigscreen.service.weishu_pdmi.ArticleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信文章service实现类
 *
 * @author ceek
 * @create 2019-08-18 15:30
 **/
@Service
public class ArticleServiceImpl implements ArticleService {
    private Logger logger = Logger.getLogger(ArticleServiceImpl.class);

    @Autowired
    private ArticleMapper articleMapper;

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
    public Page<PubArticleDTO> getPubArticlelist(List<Integer> pubAccountIdList, Integer headType, String startPublishTime, String endPublishTime, String sortName, String sortBy) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pubAccountIdList", pubAccountIdList);
        paramMap.put("headType", headType);
        paramMap.put("startPublishTime", startPublishTime);
        paramMap.put("endPublishTime", endPublishTime);
        paramMap.put("sortName", sortName);
        paramMap.put("sortBy", sortBy);
        return articleMapper.getPubArticlelist(paramMap);
    }
}
