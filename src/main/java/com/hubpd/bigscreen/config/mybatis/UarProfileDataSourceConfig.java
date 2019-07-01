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
 * 第三个数据源
 *
 * @create 2018-09-17 16:32
 **/
@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = UarProfileDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "uarProfileSqlSessionFactory")
public class UarProfileDataSourceConfig {
    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.hubpd.bigscreen.mapper.uar_profile";
    static final String MAPPER_LOCATION = "classpath:mapper/uar_profile/*.xml";

    @Value("${origin_return.datasource.url}")
    private String url;

    @Value("${origin_return.datasource.username}")
    private String user;

    @Value("${origin_return.datasource.password}")
    private String password;

    @Value("${origin_return.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "uarProfileDataSource")
    public DataSource uarProfileDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "uarProfileTransactionManager")
    public DataSourceTransactionManager uarProfileTransactionManager() {
        return new DataSourceTransactionManager(uarProfileDataSource());
    }

    @Bean(name = "uarProfileSqlSessionFactory")
    public SqlSessionFactory uarProfileSqlSessionFactory(@Qualifier("uarProfileDataSource") DataSource uarProfileDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(uarProfileDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(UarProfileDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
