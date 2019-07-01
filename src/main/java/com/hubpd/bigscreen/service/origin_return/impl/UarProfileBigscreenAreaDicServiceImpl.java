package com.hubpd.bigscreen.service.origin_return.impl;

import com.hubpd.bigscreen.bean.uar_profile.UarProfileBigscreenAreaDic;
import com.hubpd.bigscreen.mapper.uar_profile.UarProfileBigscreenAreaDicMapper;
import com.hubpd.bigscreen.service.origin_return.UarProfileBigscreenAreaDicService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ceek
 * @create 2019-07-01 11:00
 **/
@Service
@Transactional
public class UarProfileBigscreenAreaDicServiceImpl implements UarProfileBigscreenAreaDicService {
    private Logger logger = Logger.getLogger(UarProfileBigscreenAreaDicServiceImpl.class);

    @Autowired
    private UarProfileBigscreenAreaDicMapper uarProfileBigscreenAreaDicMapper;

    /**
     * 获取系统中所有的地域以及对应显示的地域地点集合
     *
     * @return
     */
    public List<UarProfileBigscreenAreaDic> findAll() {
        return uarProfileBigscreenAreaDicMapper.findAll();
    }

    /**
     * 获取系统中所有的地域以及对应显示的地域地点map
     *
     * @return
     */
    public Map<String, String> getAllProfileProvinceAndShouNameDic() {
        Map<String, String> provinceAndShowNameMap = new HashMap<String, String>();
        List<UarProfileBigscreenAreaDic> all = uarProfileBigscreenAreaDicMapper.findAll();
        for (UarProfileBigscreenAreaDic uarProfileBigscreenAreaDic : all) {
            provinceAndShowNameMap.put(uarProfileBigscreenAreaDic.getProvince(), uarProfileBigscreenAreaDic.getShowname());
        }
        return provinceAndShowNameMap;
    }
}
