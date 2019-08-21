package com.hubpd.bigscreen.controller;

import com.github.pagehelper.Page;
import com.google.common.base.CaseFormat;
import com.hubpd.bigscreen.dto.PubArticleDTO;
import com.hubpd.bigscreen.dto.PubRankDTO;
import com.hubpd.bigscreen.dto.SelfPubRankDTO;
import com.hubpd.bigscreen.service.weishu_pdmi.WXService;
import com.hubpd.bigscreen.utils.ErrorCode;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信分析-泉州晚报大屏接口
 * Created by ceek on 2018-08-07 23:14.
 */
@Controller
@RequestMapping(value="/wx")
public class WXController {
    private Logger logger = Logger.getLogger(WXController.class);

    @Autowired
    private WXService wxService;

    /**
     * 微信运营接口：微信公号、日期、新关注人数、取消关注人数、净增关注人数、累积关注人数、点赞总数； 数据是前一天往前7天的数据，每天调取1次，每个客户按最多10个公众号预估
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/getWXUserAnalyse")
    public Map<String, Object> getWXUserAnalyse(HttpServletRequest request, HttpServletResponse response){
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> resultMap = new HashMap<String, Object>();

        String orginIdStr = request.getParameter("orginId");
        String searchDateStr = request.getParameter("searchDate");

        Date searchDate = null;
        if(StringUtils.isBlank(orginIdStr)) {
            resultMap.put("code", 0);
            resultMap.put("message", "机构id未传递");
            return resultMap;
        }
        if(StringUtils.isBlank(searchDateStr)) {
            searchDate = new Date();
        } else {
            try {
                searchDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDateStr.trim());
                //当查询时间大于当天系统时间，默认查询今天
                if(searchDate.getTime() > System.currentTimeMillis()) {
                    searchDate =  new Date();
                }
            } catch (ParseException e) {
                logger.error("getWXUserAnalyse微信运营接口日期参数格式错误", e);
                resultMap.put("code", 0);
                resultMap.put("message", "日期格式错误！【yyyy-MM-dd】");
                return resultMap;
            }
        }

        orginIdStr = orginIdStr.trim();
        logger.info("微信运营接口调用，机构id为【" + orginIdStr + "】");

        try {
            return wxService.getWXUserAnalyse(orginIdStr, searchDate);
        } catch (Exception e) {
            logger.error("getWXUserAnalyse微信运营接口调用失败-发生未知错误", e);
            resultMap.put("code", 0);
            resultMap.put("message", "接口调用失败，请重试！！");
            return resultMap;
        }
    }


    /**
     * 内容ID、文章标题、文章链接（固定连接）、阅读数、点赞数
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/getWXArticleList")
    public Map<String, Object> getWXArticleList(HttpServletRequest request, HttpServletResponse response){
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        Map<String, Object> resultMap = new HashMap<String, Object>();

        String orginIdStr = request.getParameter("orginId");
        String searchDateStr = request.getParameter("searchDate");

        Date searchDate = null;
        if(StringUtils.isBlank(orginIdStr)) {
            resultMap.put("code", 0);
            resultMap.put("message", "机构id未传递");
            return resultMap;
        }
        if(StringUtils.isBlank(searchDateStr)) {
            searchDate = new Date();
        } else {
            try {
                searchDate = new SimpleDateFormat("yyyy-MM-dd").parse(searchDateStr.trim());
                //当查询时间大于当天系统时间，默认查询今天
                if(searchDate.getTime() > System.currentTimeMillis()) {
                    searchDate =  new Date();
                }
            } catch (ParseException e) {
                logger.error("getWXUserAnalyse微信内容接口日期参数格式错误", e);
                resultMap.put("code", 0);
                resultMap.put("message", "日期格式错误！【yyyy-MM-dd】");
                return resultMap;
            }
        }

        orginIdStr = orginIdStr.trim();
        logger.info("微信内容接口调用，机构id为【" + orginIdStr + "】");

        try {
            return wxService.getWXArticleList(orginIdStr, searchDate);
        } catch (Exception e) {
            logger.error("getWXUserAnalyse微信内容接口调用失败-发生未知错误", e);
            resultMap.put("code", 0);
            resultMap.put("message", "接口调用失败，请重试！！");
            return resultMap;
        }
    }

    /**
     * 公众号榜单列表信息公众号榜单
     *
     * @return
     */
    @RequestMapping(value = "/pub/rank/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listPubRank(HttpServletRequest request, HttpServletResponse response) {
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        logger.info("调用/pub/rank/list接口request param 【" + request.getQueryString() + "】");

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String orginIdStr = StringUtils.isNotBlank(request.getParameter("orginId")) ? request.getParameter("orginId").trim() : request.getParameter("orginId");
            String dayTypeStr = request.getParameter("dayType"); // 查询日期范围7/30
            String sortName = request.getParameter("sortName"); // 排序字段
            String sortBy = request.getParameter("sortBy"); // 升序或者降序
            String pageNumStr = request.getParameter("pageNum"); // 页码
            String pageSizeStr = request.getParameter("pageSize"); // 页面显示记录数
            String userFollowStr = request.getParameter("userFollow"); // 关注类型1:自有；2:关注；其它:全部


            Integer pageNum = StringUtils.isBlank(pageNumStr) ? 1 : Integer.parseInt(pageNumStr.trim());
            Integer pageSize = StringUtils.isBlank(pageSizeStr) ? 10 : Integer.parseInt(pageSizeStr.trim());
            Integer dayType = StringUtils.isBlank(dayTypeStr) ? 7 : Integer.parseInt(dayTypeStr.trim());
            sortName = StringUtils.isBlank(sortName) ? "impactIndex" : sortName.trim();    //默认根据impact_index字段排序
            sortBy = StringUtils.isBlank(sortBy) ? "DESC" : sortBy.trim();              //默认为降序

            //排序集合封装，用于验证前台传递排序字段
            ArrayList<String> sortNameList = new ArrayList<String>();
            sortNameList.add("readTotal");
            sortNameList.add("articleTotal");
            sortNameList.add("likeTotal");
            sortNameList.add("likeAverage");
            sortNameList.add("readAverage");
            sortNameList.add("headlineReadAverage");
            sortNameList.add("impactIndex");

            if (!sortNameList.contains(sortName.trim())) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "排序字段错误");
                return resultMap;
            }
            //对于排序字段驼峰格式转为下划线格式，方便mapper文件sql执行
            sortName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortName);
            if (!"asc".equalsIgnoreCase(sortBy) && !"desc".equalsIgnoreCase(sortBy)) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "sortBy关键字错误");
                return resultMap;
            }
            if (pageNum < 1) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "首页pageNum为1");
                return resultMap;
            }
            if (!(dayType == 7 || dayType == 30)) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "天只支持7或30查询");
                return resultMap;
            }

            if (StringUtils.isBlank(orginIdStr)) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "request param orginId lack");
                return resultMap;
            }
            Integer userFollow = 0;
            if (StringUtils.isBlank(userFollowStr)) {
                userFollowStr = "0";
            } else {
                try {
                    userFollow = Integer.parseInt(userFollowStr.trim());
                } catch (NumberFormatException e) {
                    resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                    resultMap.put("message", "userFollow参数格式错误");
                    return resultMap;
                }
            }
            //当不是自有和关注时，默认查询全部
            if (userFollow != 1 && userFollow != 2) {
                userFollow = 0;
            }

            Page<PubRankDTO> pubRankDTOPage = wxService.queryWechatPubRankList(orginIdStr, userFollow, dayType, pageNum, pageSize, sortName, sortBy);
            resultMap.put("totalCount", pubRankDTOPage.getTotal());
            resultMap.put("totalPage", pubRankDTOPage.getPages());
            resultMap.put("currentPageNum", pubRankDTOPage.getPageNum());
            resultMap.put("data", pubRankDTOPage.getResult());
            return resultMap;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resultMap.put("code", ErrorCode.ERROR_CODE_NOT_FOUND_MEDIA_ID);
            resultMap.put("message", "请求参数格式错误");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", ErrorCode.ERROR_CODE_NOT_FOUND_MEDIA_ID);
            resultMap.put("msg", "获取公众号榜单列表信息接口失败");
            return resultMap;
        }
    }

    /**
     * 自有公众号榜单列表信息公众号榜单
     *
     * @return
     */
    @RequestMapping(value = "/selfPub/rank/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> listSelfPubRank(HttpServletRequest request, HttpServletResponse response) {
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        logger.info("调用/selfPub/rank/list接口request param 【" + request.getQueryString() + "】");

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String orginIdStr = StringUtils.isNotBlank(request.getParameter("orginId")) ? request.getParameter("orginId").trim() : request.getParameter("orginId");
            String dayTypeStr = request.getParameter("dayType"); // 查询日期范围7/30
            String sortName = request.getParameter("sortName"); // 排序字段
            String sortBy = request.getParameter("sortBy"); // 升序或者降序
            String pageNumStr = request.getParameter("pageNum"); // 页码
            String pageSizeStr = request.getParameter("pageSize"); // 页面显示记录数

            Integer pageNum = StringUtils.isBlank(pageNumStr) ? 1 : Integer.parseInt(pageNumStr.trim());
            Integer pageSize = StringUtils.isBlank(pageSizeStr) ? 10 : Integer.parseInt(pageSizeStr.trim());
            Integer dayType = StringUtils.isBlank(dayTypeStr) ? 7 : Integer.parseInt(dayTypeStr.trim());
            sortName = StringUtils.isBlank(sortName) ? "cumulateUser" : sortName.trim();    //默认根据累计关注人数字段排序
            sortBy = StringUtils.isBlank(sortBy) ? "DESC" : sortBy.trim();              //默认为降序

            //排序集合封装，用于验证前台传递排序字段
            ArrayList<String> sortNameList = new ArrayList<String>();
            sortNameList.add("articleReadNum");
            sortNameList.add("articleLikeNum");
            sortNameList.add("newSum");
            sortNameList.add("cancelSum");
            sortNameList.add("cumulateUser");

            if (!sortNameList.contains(sortName.trim())) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "排序字段错误");
                return resultMap;
            }
//            //对于排序字段驼峰格式转为下划线格式，方便mapper文件sql执行
//            sortName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, sortName);
            if (!"asc".equalsIgnoreCase(sortBy) && !"desc".equalsIgnoreCase(sortBy)) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "sortBy关键字错误");
                return resultMap;
            }
            if (pageNum < 1) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "首页pageNum为1");
                return resultMap;
            }
            if (!(dayType == 7 || dayType == 30)) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "天只支持7或30查询");
                return resultMap;
            }

            if (StringUtils.isBlank(orginIdStr)) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "request param orginId lack");
                return resultMap;
            }

            Page<SelfPubRankDTO> selfPubRankDTOPage = wxService.queryWechatSelfPubRankList(orginIdStr, dayType, pageNum, pageSize, sortName, sortBy);
            resultMap.put("totalCount", selfPubRankDTOPage.getTotal());
            resultMap.put("totalPage", selfPubRankDTOPage.getPages());
            resultMap.put("currentPageNum", selfPubRankDTOPage.getPageNum());
            resultMap.put("data", selfPubRankDTOPage.getResult());
            return resultMap;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_ERROR_PATTERN);
            resultMap.put("message", "请求参数格式错误");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_ERROR_PATTERN);
            resultMap.put("message", "获取自有公众号榜单列表信息接口失败");
            return resultMap;
        }
    }

    /**
     * 公众号文章列表
     *
     * @return
     */
    @RequestMapping(value = "/pub/articlelist", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> pubArticlelist(HttpServletRequest request, HttpServletResponse response) {
        // 解决跨域问题
        response.setHeader("Access-Control-Allow-Origin", "*");

        logger.info("调用/pub/articlelist接口request param 【" + request.getQueryString() + "】");

        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String orginIdStr = StringUtils.isNotBlank(request.getParameter("orginId")) ? request.getParameter("orginId").trim() : request.getParameter("orginId");
            String startPublishTimeStr = request.getParameter("startPublishTime"); //文章发布起始时间
            String endPublishTimeStr = request.getParameter("endPublishTime"); //文章发布截止时间
            String headTypeStr = request.getParameter("headType"); // 头条文章标识(默认为0;0:全部；1:头条；2:非头条)
            String userFollowStr = request.getParameter("userFollow"); // 关注类型1:自有；2:关注；其它:全部
            String sortName = request.getParameter("sortName"); // 排序字段
            String sortBy = request.getParameter("sortBy"); // 升序或者降序
            String pageNumStr = request.getParameter("pageNum"); // 页码
            String pageSizeStr = request.getParameter("pageSize"); // 页面显示记录数

            Integer pageNum = StringUtils.isBlank(pageNumStr) ? 1 : Integer.parseInt(pageNumStr.trim());
            Integer pageSize = StringUtils.isBlank(pageSizeStr) ? 10 : Integer.parseInt(pageSizeStr.trim());
            Integer headType = StringUtils.isBlank(headTypeStr) ? 0 : Integer.parseInt(headTypeStr.trim());
            sortName = StringUtils.isBlank(sortName) ? "readNum" : sortName.trim();    //默认根据阅读数字段排序
            sortBy = StringUtils.isBlank(sortBy) ? "DESC" : sortBy.trim();              //默认为降序

            //排序集合封装，用于验证前台传递排序字段
            ArrayList<String> sortNameList = new ArrayList<String>();
            sortNameList.add("publishTime");
            sortNameList.add("readTotal");
            sortNameList.add("likeTotal");

            //默认查询前8天到昨天的数据
            String endPublishTime = new SimpleDateFormat("yyyy-MM-dd").format((new DateTime()).minusDays(1).withHourOfDay(23).
                    withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999).toDate());
            String startPublishTime = new SimpleDateFormat("yyyy-MM-dd").format((new DateTime()).minusDays(7).withHourOfDay(0).
                    withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0).toDate());

            if (StringUtils.isNotBlank(startPublishTimeStr)) {
                startPublishTime = startPublishTimeStr.trim();
                try {
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startPublishTime + " 00:00:00");
                } catch (ParseException e) {
                    resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                    resultMap.put("message", "起始时间格式错误【yyyy-MM-dd】");
                    return resultMap;
                }
            }
            if (StringUtils.isNotBlank(endPublishTimeStr)) {
                endPublishTime = endPublishTimeStr.trim();
                try {
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endPublishTime + " 23:59:59");
                } catch (ParseException e) {
                    resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                    resultMap.put("message", "截止时间格式错误【yyyy-MM-dd】");
                    return resultMap;
                }
            }

            if (!sortNameList.contains(sortName.trim())) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "排序字段错误");
                return resultMap;
            }
            if (!"asc".equalsIgnoreCase(sortBy) && !"desc".equalsIgnoreCase(sortBy)) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "sortBy关键字错误");
                return resultMap;
            }
            if (pageNum < 1) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "首页pageNum为1");
                return resultMap;
            }
            if (!(headType == 1 || headType == 2 || headType == 0)) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "头条标识关键字错误");
                return resultMap;
            }

            if (StringUtils.isBlank(orginIdStr)) {
                resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                resultMap.put("message", "request param orginId lack");
                return resultMap;
            }

            Integer userFollow = 0;
            if (StringUtils.isBlank(userFollowStr)) {
                userFollowStr = "0";
            } else {
                try {
                    userFollow = Integer.parseInt(userFollowStr.trim());
                } catch (NumberFormatException e) {
                    resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_NOT_FOUND);
                    resultMap.put("message", "userFollow参数格式错误");
                    return resultMap;
                }
            }
            //当不是自有和关注时，默认查询全部
            if (userFollow != 1 && userFollow != 2) {
                userFollow = 0;
            }

            Page<PubArticleDTO> articleDTOPage = wxService.getPubArticlelist(orginIdStr, headType, userFollow, startPublishTime, endPublishTime, pageNum, pageSize, sortName, sortBy);
            resultMap.put("totalCount", articleDTOPage.getTotal());
            resultMap.put("totalPage", articleDTOPage.getPages());
            resultMap.put("currentPageNum", articleDTOPage.getPageNum());
            resultMap.put("data", articleDTOPage.getResult());
            return resultMap;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            resultMap.put("code", ErrorCode.ERROR_CODE_PARAM_ERROR_PATTERN);
            resultMap.put("message", "请求参数格式错误");
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", ErrorCode.ERROR_CODE_NOT_FOUND_MEDIA_ID);
            resultMap.put("message", "获取公众号文章列表接口失败");
            return resultMap;
        }
    }
}
