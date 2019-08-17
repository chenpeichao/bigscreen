package com.hubpd.bigscreen.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * es配置产量类
 *
 * @author cpc
 * @create 2019-04-09 10:34
 **/
@Component
public class ESConfigConstants {
    /** 运营分析es常量配置 */
    /**
     * 集群名称
     */
    public static String ES_STATISTIC_CLUSTER_NAME;
    /**
     * 服务器及端口号
     */
    public static String ES_STATISTIC_TRANSPORT_NODES;
    /**
     * index索引
     */
    public static String ES_STATISTIC_INDEX_ITEM_STATISTIC;
    /**
     * type类型
     */
    public static String ES_STATISTIC_TYPE_ITEM_BASIC_STATISTIC;

    /** 用户画像es常量配置 */
    /**
     * 集群名称
     */
    public static String ES_PROFILE_CLUSTER_NAME;
    /**
     * 服务器及端口号
     */
    public static String ES_PROFILE_TRANSPORT_NODES;
    /**
     * index索引
     */
    public static String ES_PROFILE_INDEX_OFFLINE_USER_PROFILE;
    /**
     * type类型
     */
    public static String ES_PROFILE_TYPE_USER_TAGS;


    @Value("${es_statistic_cluster_name}")
    public void setEsStatisticClusterName(String esStatisticClusterName) {
        this.ES_STATISTIC_CLUSTER_NAME = esStatisticClusterName;
    }

    @Value("${es_statistic_transport_nodes}")
    public void setEsStatisticTransportNodes(String esStatisticTransportNodes) {
        this.ES_STATISTIC_TRANSPORT_NODES = esStatisticTransportNodes;
    }

    @Value("${es_statistic_index_item_statistic}")
    public void setEsStatisticIndexItemStatistic(String esStatisticIndexItemStatistic) {
        this.ES_STATISTIC_INDEX_ITEM_STATISTIC = esStatisticIndexItemStatistic;
    }

    @Value("${es_statistic_type_item_basic_statistic}")
    public void setEsStatisticTypeItemBasicStatistic(String esStatisticTypeItemBasicStatistic) {
        this.ES_STATISTIC_TYPE_ITEM_BASIC_STATISTIC = esStatisticTypeItemBasicStatistic;
    }


    @Value("${es_profile_cluster_name}")
    public void setEsProfileClusterName(String esProfileClusterName) {
        this.ES_PROFILE_CLUSTER_NAME = esProfileClusterName;
    }

    @Value("${es_profile_transport_nodes}")
    public void setEsProfileTransportNodes(String esProfileTransportNodes) {
        this.ES_PROFILE_TRANSPORT_NODES = esProfileTransportNodes;
    }

    @Value("${es_profile_index_offline_user_profile}")
    public void setEsPprofileIndexOfflineUserProfile(String esPprofileIndexOfflineUserProfile) {
        this.ES_PROFILE_INDEX_OFFLINE_USER_PROFILE = esPprofileIndexOfflineUserProfile;
    }

    @Value("${es_profile_type_user_tags}")
    public void setEsProfileTypeUserTags(String esProfileTypeUserTags) {
        this.ES_PROFILE_TYPE_USER_TAGS = esProfileTypeUserTags;
    }
}
