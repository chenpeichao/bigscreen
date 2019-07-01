package com.hubpd.bigscreen.service.origin_return;

import com.hubpd.bigscreen.bean.uar_profile.UarProfileBigscreenAreaDic;

import java.util.List;
import java.util.Map;

/**
 * uar针对大屏地域显示字典service
 *
 * @author ceek
 * @create 2019-06-13 11:11
 **/
public interface UarProfileBigscreenAreaDicService {
    /**
     * 获取系统中所有的地域以及对应显示的地域地点集合
     *
     * @return
     */
    public List<UarProfileBigscreenAreaDic> findAll();

    /**
     * 获取系统中所有的地域以及对应显示的地域地点map
     *
     * @return
     */
    public Map<String, String> getAllProfileProvinceAndShouNameDic();
}
