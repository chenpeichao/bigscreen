package com.hubpd.bigscreen.mapper.uar_profile;

import com.hubpd.bigscreen.bean.uar_profile.UserAnalyse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用户分析--性别，年龄，地域(-泉州晚报大屏接口)
 * @author cpc
 * @create 2018-08-11 20:09
 **/
@Component
public interface UserAnalyseMapper extends ElasticsearchRepository<UserAnalyse, String> {
    @Query("{\"bool\" : {\"must\" : [{\"range\" : {\"tag_count\" : {\"gt\": \"0\"}}},{\"term\" : {\"age\" : \"?0\"}},{\"query_string\" : {\"default_field\" : \"user_tags.at\",\"query\" : \"at : ?1\"}}]}}")
    public List findByAtAndTagCountAndAge(String age, String at, Pageable pageable);
}
