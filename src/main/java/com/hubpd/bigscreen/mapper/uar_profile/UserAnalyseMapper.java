package com.hubpd.bigscreen.mapper.uar_profile;

import com.hubpd.bigscreen.bean.uar_profile.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * 用户分析--性别，年龄，地域(-泉州晚报大屏接口)
 * @author cpc
 * @create 2018-08-11 20:09
 **/
@Component
public interface UserAnalyseMapper extends ElasticsearchRepository<Article, String> {
}
