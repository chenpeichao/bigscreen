package com.hubpd.bigscreen.config;

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
 * 第一个数据源
 * Created by ceek on 2018-08-08 21:50.
 */
@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = WeishuPdmiDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "weishuPdmiSqlSessionFactory")
public class WeishuPdmiDataSourceConfig {
    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.hubpd.bigscreen.mapper.weishu_pdmi";
    static final String MAPPER_LOCATION = "classpath:mapper/weishu_pdmi/*.xml";

    @Value("${weishu_pdmi.datasource.url}")
    private String url;

    @Value("${weishu_pdmi.datasource.username}")
    private String user;

    @Value("${weishu_pdmi.datasource.password}")
    private String password;

    @Value("${weishu_pdmi.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "weishuPdmiDataSource")
    @Primary
    public DataSource weishuPdmiDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "weishuPdmiTransactionManager")
    @Primary
    public DataSourceTransactionManager weishuPdmiTransactionManager() {
        return new DataSourceTransactionManager(weishuPdmiDataSource());
    }

    @Bean(name = "weishuPdmiSqlSessionFactory")
    @Primary
    public SqlSessionFactory weishuPdmiSqlSessionFactory(@Qualifier("weishuPdmiDataSource") DataSource weishuPdmiDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(weishuPdmiDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(WeishuPdmiDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
