package com.hubpd.bigscreen.config.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 第二个数据源
 * Created by ceek on 2018-08-08 21:56.
 */
@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = UarBasicDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "uarBasicSqlSessionFactory")
public class UarBasicDataSourceConfig {
    // 精确到 cluster 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.hubpd.bigscreen.mapper.uar_basic";
    static final String MAPPER_LOCATION = "classpath:mapper/uar_basic/*.xml";

    @Value("${uar_basic.datasource.url}")
    private String url;

    @Value("${uar_basic.datasource.username}")
    private String user;

    @Value("${uar_basic.datasource.password}")
    private String password;

    @Value("${uar_basic.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "uarBasicDataSource")
    public DataSource uarBasicDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "uarBasicTransactionManager")
    public DataSourceTransactionManager uarBasicTransactionManager() {
        return new DataSourceTransactionManager(uarBasicDataSource());
    }

    @Bean(name = "uarBasicSqlSessionFactory")
    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("uarBasicDataSource") DataSource uarBasicDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(uarBasicDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(UarBasicDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
