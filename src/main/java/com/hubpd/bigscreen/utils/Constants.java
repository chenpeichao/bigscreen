package com.hubpd.bigscreen.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 常量工具类
 * Created by ceek on 2018-08-09 22:45.
 */
@Component
public class Constants {
    /** 数据回溯指定天的数据--开始时间 */
    public static Integer DATA_BACK_BEGIN_DAY_NUM;
    /** 数据回溯指定天的数据--结束时间 */
    public static Integer DATA_BACK_END_DAY_NUM;


    /** uar画像es配置 */
    public static String PROFILE_ES_INDEX;
    public static String PROFILE_ES_TYPE;


    /** 对外接口相关配置信息 */
    /**
     * 对外接口主url
     */
    public static String PDMI_INTERFACE_HOST;
    /**
     * 根据uar中的机构id获取crt中机构id的接口url
     */
    public static String PDMI_INTERFACE_URL_GET_ADMIN_ID_BY_UAR_ORG_ID;
    /**
     * 根据机构id以及媒体类型获取crt中指定媒体类型下的转载和原创数据
     */
    public static String PDMI_INTERFACE_URL_GET_CRT_INFO_BY_ADMIN_ID_AND_MEDIA_TYPE;
    /**
     * 根据uar机构id获取uar中appkey和media的对应关系
     */
    public static String PDMI_INTERFACE_URL_CRT_MEDIA_RELATION_BY_UAR_ORG_ID;

    @Value("${data_back_begin_day_num}")
    public void setDataBackBeginDayNum(Integer dataBackBeginDayNum) {
        this.DATA_BACK_BEGIN_DAY_NUM = dataBackBeginDayNum;
    }
    @Value("${data_back_end_day_num}")
    public void setDataBackEndDayNum(Integer dataBackEndDayNum) {
        this.DATA_BACK_END_DAY_NUM = dataBackEndDayNum;
    }


    @Value("${profile_es_index}")
    public void setProfileEsIndex(String profileEsIndex) {
        this.PROFILE_ES_INDEX = profileEsIndex;
    }
    @Value("${profile_es_type}")
    public void setProfileEsType(String profileEsType) {
        this.PROFILE_ES_TYPE = profileEsType;
    }


    @Value("${pdmi_interface_host}")
    public void setPdmiInterfaceHost(String pdmiInterfaceHost) {
        this.PDMI_INTERFACE_HOST = pdmiInterfaceHost;
    }

    @Value("${pdmi_interface_url_get_admin_id_by_uar_org_id}")
    public void setPdmiInterfaceUrlGetAdminIdByUarOrgId(String pdmiInterfaceUrlGetAdminIdByUarOrgId) {
        this.PDMI_INTERFACE_URL_GET_ADMIN_ID_BY_UAR_ORG_ID = pdmiInterfaceUrlGetAdminIdByUarOrgId;
    }

    @Value("${pdmi_interface_url_get_crt_info_by_admin_id_and_media_type}")
    public void setPdmiInterfaceUrlGetCrtInfoByAdminIdAndMediaType(String pdmiInterfaceUrlGetCrtInfoByAdminIdAndMediaType) {
        this.PDMI_INTERFACE_URL_GET_CRT_INFO_BY_ADMIN_ID_AND_MEDIA_TYPE = pdmiInterfaceUrlGetCrtInfoByAdminIdAndMediaType;
    }

    @Value("${pdmi_interface_url_crt_media_relation_by_uar_org_id}")
    public void setPdmiInterfaceUrlCrtMediaRelationByUarOrgId(String pdmiInterfaceUrlCrtMediaRelationByUarOrgId) {
        this.PDMI_INTERFACE_URL_CRT_MEDIA_RELATION_BY_UAR_ORG_ID = pdmiInterfaceUrlCrtMediaRelationByUarOrgId;
    }
}
