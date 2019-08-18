package com.hubpd.bigscreen.service.weishu_pdmi;

import com.github.pagehelper.Page;
import com.hubpd.bigscreen.dto.PubRankDTO;
import com.hubpd.bigscreen.dto.SelfPubRankDTO;

import java.util.Date;
import java.util.Map;

/**
 * 微信service
 *
 * @author cpc
 * @create 2018-08-09 11:08
 **/
public interface WXService {
    /**
     * 根据机构id查询其下用户授权的公众号的前7天的公众号用户分析信息
     * @param orginId       机构Id
     * @param searchDate    查询日期  yyyy-MM-dd
     * @return
     */
    public Map<String, Object> getWXUserAnalyse(String orginId, Date searchDate);

    /**
     * 根据机构id查询其下用户授权的公众号的前7天的公众号发文的内容ID、文章标题、文章链接（固定连接）、阅读数、点赞数
     * @param orginId       机构Id
     * @param searchDate    查询日期  yyyy-MM-dd
     * @return
     */
    public Map<String, Object> getWXArticleList(String orginId, Date searchDate);

    /**
     * 查询指定天范围的微信榜单数据列表
     *
     * @param orginIdStr 租户id
     * @param userFollow 用户关注类型0:全部；1:自有；2:关注
     * @param dayType    查询日期范围，现在只支持7/30
     * @param pageNum    页码
     * @param pageSize   每页显示条数
     * @param sortName   排序字段
     * @param sortBy     升序/降序
     * @return
     */
    public Page<PubRankDTO> queryWechatPubRankList(String orginIdStr, Integer userFollow, Integer dayType,
                                                   Integer pageNum, Integer pageSize, String sortName, String sortBy);

    /**
     * 查询指定天范围的自有微信榜单数据列表
     *
     * @param orginIdStr 租户id
     * @param dayType    查询日期范围，现在只支持7/30
     * @param pageNum    页码
     * @param pageSize   每页显示条数
     * @param sortName   排序字段
     * @param sortBy     升序/降序
     * @return
     */
    public Page<SelfPubRankDTO> queryWechatSelfPubRankList(String orginIdStr, Integer dayType,
                                                           Integer pageNum, Integer pageSize, String sortName, String sortBy);
}
