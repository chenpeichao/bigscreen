package com.hubpd.bigscreen.service.weishu_pdmi.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hubpd.bigscreen.bean.weishu_pdmi.PubAccount;
import com.hubpd.bigscreen.bean.weishu_pdmi.PubAccountWithBLOBs;
import com.hubpd.bigscreen.mapper.weishu_pdmi.PubAccountMapper;
import com.hubpd.bigscreen.service.uar_basic.UarBasicUserService;
import com.hubpd.bigscreen.service.weishu_pdmi.WXService;
import com.hubpd.bigscreen.service.weishu_pdmi.WeiShuPdmiUserService;
import com.hubpd.bigscreen.utils.Constants;
import com.hubpd.bigscreen.utils.DateUtils;
import com.hubpd.bigscreen.vo.WXArticleAnalyseVO;
import com.hubpd.bigscreen.vo.WXUserAnalyseVO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.hubpd.bigscreen.utils.DateUtils.getBeforeDateStrByDateAndPattern;

/**
 * 微信service
 *
 * @author cpc
 * @create 2018-08-09 11:08
 **/
@Service
@Transactional
public class WXServiceImpl implements WXService {
    private Logger logger = Logger.getLogger(WXServiceImpl.class);

    /** uar_basic用户service */
    @Autowired
    private UarBasicUserService uarBasicUserService;
    /** wei_shu对应的微信用户service */
    @Autowired
    private WeiShuPdmiUserService weiShuPdmiUserService;



    /** wei_shu对应的公众号 */
    @Autowired
    private PubAccountMapper pubAccountMapper;

    /**
     * 根据机构id查询其下用户授权的公众号的前7天的公众号用户分析信息
     * @param orginId       机构Id
     * @param searchDate    查询日期  yyyy-MM-dd
     * @return
     */
    public Map<String, Object> getWXUserAnalyse(String orginId, Date searchDate) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 1、查询uar环境中指定机构下的用户id列表
        List<String> uarBasicUserIdListByOrginId = uarBasicUserService.findUarBasicUserIdListByOrginId(orginId);

        // 2、根据用户id列表查询其对应的公众号列表（1:自有，2：关注）
        List<Integer> pubAccountIdListByUserIdList = new ArrayList<Integer>();
        if(uarBasicUserIdListByOrginId.size() > 0) {
            pubAccountIdListByUserIdList = weiShuPdmiUserService.findPubAccountIdListByUserIdList(uarBasicUserIdListByOrginId, 1);
        }
        // 2.1、对于机构对应的用户以及公众号进行打印
        logger.info("在【"+DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss") +"】查询机构id为【"+orginId+"】" + "对应用户id【"+uarBasicUserIdListByOrginId.toString()+"】，对应自有公众号id为【"+pubAccountIdListByUserIdList.toString()+"】");

        // 2.2、定义查询的起始/结束时间，传递时间的前7天---昨天的数据
        String beginDateStr = "";
        String endDateStr = "";
        try {
            beginDateStr = DateUtils.getBeforeDateStrByDateAndPattern(searchDate, Constants.DATA_BACK_BEGIN_DAY_NUM, "yyyy-MM-dd");
            endDateStr = DateUtils.getBeforeDateStrByDateAndPattern(searchDate, Constants.DATA_BACK_END_DAY_NUM, "yyyy-MM-dd");
        } catch (Exception e) {
            logger.error("请求参数，时间转换错误", e);
            resultMap.put("code", 0);
            resultMap.put("message", "接口调用失败，时间参数传递错误！！");
            return resultMap;
        }

        // 3、构造中间封装结果的Map<微信号_日期，用户分析实体>，用来冗余数据库中某些公众号对于用户信息可能没有数据，按0填充
        Map<String, WXUserAnalyseVO> installMap = new TreeMap<String, WXUserAnalyseVO>();

        // 3.1、对于公众号进行初始化Map封装
        for(Integer pubAccountId : pubAccountIdListByUserIdList) {
            Date date = org.apache.commons.lang3.time.DateUtils.addDays(searchDate, Constants.DATA_BACK_BEGIN_DAY_NUM);

            // 3.2、根据公众号id查询公众号详细的信息
            PubAccount pubAccount = pubAccountMapper.selectByIdNoBlobColumn(pubAccountId);

            // 3.3、对于公众号的key(微信号_日期(前7天))，以及value(指定公众号在指定天的用户分析数据)，进行Map初始化
            if(pubAccount != null) {
                // 3.4、遍历前7天，封装map
                while (date.compareTo(org.apache.commons.lang3.time.DateUtils.addDays(searchDate, Constants.DATA_BACK_END_DAY_NUM)) <= 0) {
                    WXUserAnalyseVO wxUserAnalyseVO = new WXUserAnalyseVO();
                    wxUserAnalyseVO.setWxNo(pubAccount.getWxId());
                    wxUserAnalyseVO.setWxName(pubAccount.getWxName());
                    wxUserAnalyseVO.setDateTime(DateUtils.getDateStrByDate(date, "yyyy-MM-dd"));
                    installMap.put(pubAccount.getWxId() + "_" + DateUtils.getDateStrByDate(date, "yyyy-MM-dd"), wxUserAnalyseVO);

                    date = org.apache.commons.lang3.time.DateUtils.addDays(date, 1);
                }
            }
        }

        try {
            List<WXUserAnalyseVO> wxUserAnalyseVOList = new ArrayList<WXUserAnalyseVO>();
            if(pubAccountIdListByUserIdList.size() > 0) {
                // 4.1、对于存在的公众号，获取到公众号在指定时间段的新关注用户、取消用户等信息。
                wxUserAnalyseVOList = weiShuPdmiUserService.findUserStatisticsByPubAccountIdListAndSearchDate(pubAccountIdListByUserIdList, beginDateStr, endDateStr);

                // 4.2、将用户信息(新关注用户、取消关注用户等)数据填充到开始初始化的Map中
                for(WXUserAnalyseVO wxUserAnalyseVO : wxUserAnalyseVOList) {
                    WXUserAnalyseVO wxUserAnalyseVOFromMap = installMap.get(wxUserAnalyseVO.getWxNo() + "_" + wxUserAnalyseVO.getDateTime());
                    if(wxUserAnalyseVOFromMap != null) {
                        wxUserAnalyseVOFromMap.setCancelFollowNum(wxUserAnalyseVO.getCancelFollowNum());
                        wxUserAnalyseVOFromMap.setCumulateFollowNum(wxUserAnalyseVO.getCumulateFollowNum());
                        wxUserAnalyseVOFromMap.setNewFollowNum(wxUserAnalyseVO.getNewFollowNum());
                        wxUserAnalyseVOFromMap.setRealIncreaseFollowNum(wxUserAnalyseVO.getRealIncreaseFollowNum());
                    }
                }

                // 4.3、将用户点赞数据填充到开始初始化的Map中
                wxUserAnalyseVOList = weiShuPdmiUserService.findPubAccountLikeNumByPubAccountIdListAndSearchDate(pubAccountIdListByUserIdList, beginDateStr, endDateStr);
                for(WXUserAnalyseVO wxUserAnalyseVO : wxUserAnalyseVOList) {
                    WXUserAnalyseVO wxUserAnalyseVOFromMap = installMap.get(wxUserAnalyseVO.getWxNo() + "_" + wxUserAnalyseVO.getDateTime());
                    if(wxUserAnalyseVOFromMap != null) {
                        wxUserAnalyseVOFromMap.setLikeNum(wxUserAnalyseVO.getLikeNum());
                        wxUserAnalyseVOFromMap.setReadNum(wxUserAnalyseVO.getReadNum());
                    }
                }
            }

            // 4.4、将map中封装的value(用户分析数据：每个公众号对应的查询前七天对应的新关注用户等和点赞数)放入list容器中
            List<WXUserAnalyseVO> resultWXUserAnalyseVOList = new ArrayList<WXUserAnalyseVO>();
            Set<Map.Entry<String, WXUserAnalyseVO>> entries = installMap.entrySet();
            for(Map.Entry<String, WXUserAnalyseVO> entry : entries) {
                resultWXUserAnalyseVOList.add(entry.getValue());
            }

            // 4.4.1、对用户分析数据(每个公众号对应的查询前七天对应的新关注用户等和点赞数)，按照公众号名称排序，排序后对于每个公众号再按照数据时间倒序排列
            Collections.sort(resultWXUserAnalyseVOList, new Comparator<WXUserAnalyseVO>() {
                @Override
                public int compare(WXUserAnalyseVO o1, WXUserAnalyseVO o2) {
                    if(!o1.getWxNo().equals(o2.getWxNo())) {
                        return o1.getWxNo().compareTo(o2.getWxNo());
                    } else {
                        return -o1.getDateTime().compareTo(o2.getDateTime());
                    }
                }
            });

            resultMap.put("code", 1);
            resultMap.put("data", resultWXUserAnalyseVOList);
        } catch (Exception e) {
            logger.error("查询出错，请稍后再试！", e);
            resultMap.put("code", 0);
            resultMap.put("message", "查询出错，请稍后再试！");
        } finally {
            return resultMap;
        }
    }

    /**
     * 根据机构id查询其下用户授权的公众号的前7天的公众号发文的内容ID、文章标题、文章链接（固定连接）、阅读数、点赞数
     * @param orginId       机构Id
     * @param searchDate    查询日期  yyyy-MM-dd
     * @return
     */
    public Map<String, Object> getWXArticleList(String orginId, Date searchDate) {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        // 1、查询uar环境中指定机构下的用户id列表
        List<String> uarBasicUserIdListByOrginId = uarBasicUserService.findUarBasicUserIdListByOrginId(orginId);

        // 2、根据用户id列表查询其对应的公众号列表（1:自有，2：关注）
        List<Integer> pubAccountIdListByUserIdList = new ArrayList<Integer>();
        if(uarBasicUserIdListByOrginId.size() > 0) {
            pubAccountIdListByUserIdList = weiShuPdmiUserService.findPubAccountIdListByUserIdList(uarBasicUserIdListByOrginId, 1);
        }
        // 2.1、对于机构对应的用户以及公众号进行打印
        logger.info("在【"+DateUtils.getDateStrByDate(new Date(), "yyyy-MM-dd HH:mm:ss") +"】查询机构id为【"+orginId+"】" + "对应用户id【"+uarBasicUserIdListByOrginId.toString()+"】，对应自有公众号id为【"+pubAccountIdListByUserIdList.toString()+"】");

        // 2.2、定义查询的起始/结束时间，传递时间的前7天---昨天的数据
        String beginDateStr = "";
        String endDateStr = "";
        try {
            beginDateStr = DateUtils.getBeforeDateStrByDateAndPattern(searchDate, Constants.DATA_BACK_BEGIN_DAY_NUM, "yyyy-MM-dd");
            endDateStr = DateUtils.getBeforeDateStrByDateAndPattern(searchDate, Constants.DATA_BACK_END_DAY_NUM, "yyyy-MM-dd");
        } catch (Exception e) {
            logger.error("请求参数，时间转换错误", e);
            resultMap.put("code", 0);
            resultMap.put("message", "接口调用失败，时间参数传递错误！！");
            return resultMap;
        }

        // 对于没有用户的机构id，返回空数据
        if (pubAccountIdListByUserIdList == null || pubAccountIdListByUserIdList.size() == 0) {
            resultMap.put("code", 1);
            resultMap.put("data", new ArrayList<WXArticleAnalyseVO>());
            return resultMap;
        }

        try {
            // 3、根据公众号id列表，查询文章信息以及指定日期段的阅读数和点赞数
            List<WXArticleAnalyseVO> wxArticleAnalyseVOList = weiShuPdmiUserService.findgetWXArticleStatByAccountIdListAndSearchDate(pubAccountIdListByUserIdList, beginDateStr, endDateStr);

            // 4、对微信文章数据，按照公众号名称排序，排序后对于每个公众号再按照数据时间倒序排列
            Collections.sort(wxArticleAnalyseVOList, new Comparator<WXArticleAnalyseVO>() {
                @Override
                public int compare(WXArticleAnalyseVO o1, WXArticleAnalyseVO o2) {
                    if(!o1.getWxNo().equals(o2.getWxNo())) {
                        return o1.getWxNo().compareTo(o2.getWxNo());
                    } else {
                        if(!o1.getArticleId().equals(o2.getArticleId())) {
                            return o1.getArticleId().compareTo(o2.getArticleId());
                        } else {
                            return -o1.getDateTime().compareTo(o2.getDateTime());
                        }
                    }
                }
            });

            resultMap.put("code", 1);
            resultMap.put("data", wxArticleAnalyseVOList);
        } catch (Exception e) {
            logger.error("查询出错，请稍后再试！", e);
            resultMap.put("code", 0);
            resultMap.put("message", "查询出错，请稍后再试！");
        } finally {
            return resultMap;
        }
    }
}
