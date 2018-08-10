package com.hubpd.bigscreen.mapper.weishu_pdmi;

import com.hubpd.bigscreen.bean.weishu_pdmi.PubAccount;
import com.hubpd.bigscreen.bean.weishu_pdmi.PubAccountWithBLOBs;

public interface PubAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PubAccountWithBLOBs record);

    int insertSelective(PubAccountWithBLOBs record);

    PubAccountWithBLOBs selectByPrimaryKey(Integer id);
    PubAccount selectByIdNoBlobColumn(Integer id);

    int updateByPrimaryKeySelective(PubAccountWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PubAccountWithBLOBs record);

    int updateByPrimaryKey(PubAccount record);
}