package com.hubpd.bigscreen.mapper.uar_profile;

import com.hubpd.bigscreen.bean.uar_profile.UarProfileBigscreenAreaDic;

import java.util.List;

public interface UarProfileBigscreenAreaDicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UarProfileBigscreenAreaDic record);

    int insertSelective(UarProfileBigscreenAreaDic record);

    UarProfileBigscreenAreaDic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UarProfileBigscreenAreaDic record);

    int updateByPrimaryKey(UarProfileBigscreenAreaDic record);

    /**
     * 获取系统中所有的地域以及对应显示的地域地点map
     *
     * @return
     */
    public List<UarProfileBigscreenAreaDic> findAll();
}