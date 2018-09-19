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
    /** 根据uar中的机构id获取crt中机构id的接口url */
    public static String PDMI_INTERFACE_URL_GET_ADMIN_ID_BY_UAR_ORG_ID;
    /** 根据机构id以及媒体类型获取crt中指定媒体类型下的转载和原创数据 */
    public static String PDMI_INTERFACE_URL_GET_CRT_INFO_BY_ADMIN_ID_AND_MEDIA_TYPE;
    /** 根据uar机构id获取uar中appkey和media的对应关系 */
    public static String PDMI_INTERFACE_URL_CRT_MEDIA_RELATION_BY_UAR_ORG_ID;
    /** 获取crt中的数据标识(多个以英文字符分隔)：0：网站，2：app */
    public static String PDMI_INTERFACE_URL_CRT_APP_FLAG;


    /** 小程序调用标识 */
    /** 系统验证机构分配标识 */
    public static String CLIENT_CODE;
    /** 系统验证机构标识密钥 */
    public static String SECRET_KEY;

    /** uar_basic中对于应用进行类型的标识应用类型：1:web，2:app，3:weixin，4:weibo */
    /**
     * uar中对于app为移动应用和网站的应用标识
     */
    public static Integer UAR_APP_TYPE_WEB;
    public static Integer UAR_APP_TYPE_APP;




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

    @Value("${pdmi_interface_url_crt_app_flag}")
    public void setPdmiInterfaceUrlCrtAppFlag(String pdmiInterfaceUrlCrtAppFlag) {
        this.PDMI_INTERFACE_URL_CRT_APP_FLAG = pdmiInterfaceUrlCrtAppFlag;
    }

    @Value("${client_code}")
    public void setClientCode(String clientCode) {
        this.CLIENT_CODE = clientCode;
    }

    @Value("${secret_key}")
    public void setSecretKey(String secretKey) {
        this.SECRET_KEY = secretKey;
    }

    @Value("${uar_app_type_web}")
    public void setUarAppTypeWeb(Integer uarAppTypeWeb) {
        this.UAR_APP_TYPE_WEB = uarAppTypeWeb;
    }

    @Value("${uar_app_type_app}")
    public void setUarAppTypeApp(Integer uarAppTypeApp) {
        this.UAR_APP_TYPE_APP = uarAppTypeApp;
    }

}
