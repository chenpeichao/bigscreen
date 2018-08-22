0、对外接口请求clientCode等信息生成语句；
    INSERT INTO t_resource_client(CODE, NAME, secret_key, STATUS, remark) VALUE('shunyibigscreen', '顺义大屏','z3cEW81pToU4XwAC',1,'顺义大屏借口，调用微信、微博、用户分析(画像)');
1、此种方式配置的多数据源下的@Transactional注解会生效
2、微信运营分析：sql
    SELECT
      tasd.ref_date1,
      pub_account_id AS 'pub_account_id',
      tpa.wx_id AS wx_id,
      tpa.wx_name AS wx_name,
      tasd.readNum,
      tasd.likeNum,
    IFNULL(phase1.new_sum, 0) AS 'new_sum',
    IFNULL(phase1.cancel_sum, 0) AS 'cancel_sum',
    IFNULL(phase1.real_increase, 0) AS 'real_increase',
    IFNULL(phase1.cumulate_user, 0) AS 'cumulate_user'
    FROM
      (
            SELECT uc.ref_date,us.pub_account_id,us.new_sum,us.cancel_sum,us.real_increase, uc.cumulate_user
    		FROM
    			( SELECT
    				ref_date,
    				pub_account_id,
    				SUM(new_user) AS 'new_sum',
    				SUM(cancel_user) AS 'cancel_sum',
    				SUM(new_user) - SUM(cancel_user) AS 'real_increase'
    			FROM
    				t_get_user_summary
    			WHERE
    				pub_account_id IN (11197, 9687)           #123123
    				AND  ref_date <= '2018-07-30' AND ref_date >= '2018-07-25'
    			GROUP BY ref_date,pub_account_id
    			ORDER BY ref_date,pub_account_id
    			) us
    		RIGHT JOIN (
    				SELECT
    					ref_date,
    					pub_account_id,
    					cumulate_user
    				FROM
    					t_get_user_cumulate
    				WHERE
    					pub_account_id IN (11197, 9687)            #123123
    					AND  ref_date <= '2018-07-30' AND ref_date >= '2018-07-25'
    			     ) uc ON us.pub_account_id = uc.pub_account_id AND us.ref_date = uc.ref_date
       ) phase1
       RIGHT JOIN (
    	SELECT
    		tas.pub_account_id AS 'pub_account_id1',
    		tas.ref_date AS 'ref_date1',
    		SUM(tas.total_read_num) AS 'readNum',
    		SUM(tas.total_like_num) AS 'likeNum'
    		FROM t_article_stat_day tas
    		WHERE tas.ref_date >= '2018-07-25' AND tas.ref_date <= '2018-07-30' AND tas.pub_account_id IN (11197, 9687)
    		GROUP BY  tas.pub_account_id, tas.ref_date ORDER BY tas.pub_account_id, tas.ref_date DESC
       ) tasd ON tasd.pub_account_id1 = phase1.pub_account_id AND tasd.ref_date1 = phase1.ref_date LEFT JOIN t_pub_account tpa ON tasd.pub_account_id1 = tpa.id

    ORDER BY pub_account_id, tasd.ref_date1 DESC;

3、用户分析中的地域字典表和接口返回值
CREATE TABLE `uar_tag_dic_region` (
   `id` INT(10) NOT NULL AUTO_INCREMENT COMMENT '自增主键id',
   `region_name` VARCHAR(255) NOT NULL COMMENT '省份名称',
   `status` INT(2) NOT NULL DEFAULT '1' COMMENT '失效标识；0：失效，1：生效',
   PRIMARY KEY (`id`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='省份字典表';


 CREATE TABLE `uar_origin_return_record` (
   `id` INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键自增id',
   `origin_id` VARCHAR(255) NOT NULL COMMENT '机构id',
   `return_json` TEXT NOT NULL COMMENT '返回值',
   `return_date` VARCHAR(32) NOT NULL COMMENT '返回时间',
   PRIMARY KEY (`id`)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='大屏接口调用记录表';


4、省份字典表：
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('北京','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('上海','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('重庆','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('广东','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('天津','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('浙江','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('澳门','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('广西','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('内蒙古','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('宁夏','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('江西','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('台湾','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('安徽','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('陕西','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('辽宁','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('山西','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('青海','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('香港','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('四川','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('江苏','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('河北','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('西藏','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('福建','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('吉林','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('新疆','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('海南','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('湖北','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('云南','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('甘肃','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('湖南','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('河南','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('山东','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('黑龙江','1');
    INSERT INTO `uar_tag_dic_region` (`region_name`, `status`) VALUES('贵州','1');