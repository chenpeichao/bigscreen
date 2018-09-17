package com.hubpd.bigscreen.config.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 第三个数据源
 *
 * @create 2018-09-17 16:32
 **/
@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = UarStatisticDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "uarStatisticSqlSessionFactory")
public class UarStatisticDataSourceConfig {
    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.hubpd.bigscreen.mapper.uar_statistic";
    static final String MAPPER_LOCATION = "classpath:mapper/uar_statistic/*.xml";

    @Value("${uar_statistic.datasource.url}")
    private String url;

    @Value("${uar_statistic.datasource.username}")
    private String user;

    @Value("${uar_statistic.datasource.password}")
    private String password;

    @Value("${uar_statistic.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "uarStatisticDataSource")
    public DataSource uarStatisticDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "uarStatisticTransactionManager")
    public DataSourceTransactionManager uarStatisticTransactionManager() {
        return new DataSourceTransactionManager(uarStatisticDataSource());
    }

    @Bean(name = "uarStatisticSqlSessionFactory")
    public SqlSessionFactory uarStatisticSqlSessionFactory(@Qualifier("uarStatisticDataSource") DataSource uarStatisticDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(uarStatisticDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(UarStatisticDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
