package com.hubpd.bigscreen.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 错误码参数类
 *
 * @author cpc
 * @create 2019-04-08 11:30
 **/
@Component
public class ErrorCode {
    /**
     * 请求参数未传递或为空：1001
     */
    public static Integer ERROR_CODE_PARAM_NOT_FOUND;
    /**
     * 请求参数格式错误：1002
     */
    public static Integer ERROR_CODE_PARAM_ERROR_PATTERN;
    /**
     * 请求参数大小超过默认值(30):1003
     */
    public static Integer ERROR_CODE_PARAM_MORE_THAN_DEFAULT;
    /**
     * 系统中不存在指定机构(30):1004
     */
    public static Integer ERROR_CODE_NOT_FOUND_MEDIA_ID;


    @Value("${error_code_param_not_found}")
    public void setErrorCodeParamNotFound(Integer errorCodeParamNotFound) {
        this.ERROR_CODE_PARAM_NOT_FOUND = errorCodeParamNotFound;
    }

    @Value("${error_code_param_error_pattern}")
    public void setErrorCodeParamErrorPattern(Integer errorCodeParamErrorPattern) {
        this.ERROR_CODE_PARAM_ERROR_PATTERN = errorCodeParamErrorPattern;
    }

    @Value("${error_code_param_more_than_default}")
    public void setErrorCodeParamMoreThanDefault(Integer errorCodeParamMoreThanDefault) {
        this.ERROR_CODE_PARAM_MORE_THAN_DEFAULT = errorCodeParamMoreThanDefault;
    }

    @Value("${error_code_not_found_media_id}")
    public void setErrorCodeNotFoundMediaId(Integer errorCodeNotFoundMediaId) {
        this.ERROR_CODE_NOT_FOUND_MEDIA_ID = errorCodeNotFoundMediaId;
    }
}
