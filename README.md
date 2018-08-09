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

