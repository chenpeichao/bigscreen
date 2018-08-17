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
 * 大屏接口调用记录
 *
 * @author cpc
 * @create 2018-08-15 18:48
 **/
@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = OriginReturnDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "originReturnSqlSessionFactory")
public class OriginReturnDataSourceConfig {
    // 精确到 cluster 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.hubpd.bigscreen.mapper.origin_return";
    static final String MAPPER_LOCATION = "classpath:mapper/origin_return/*.xml";

    @Value("${origin_return.datasource.url}")
    private String url;

    @Value("${origin_return.datasource.username}")
    private String user;

    @Value("${origin_return.datasource.password}")
    private String password;

    @Value("${origin_return.datasource.driverClassName}")
    private String driverClass;

    @Bean(name = "originReturnDataSource")
    public DataSource originReturnDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean(name = "originReturnTransactionManager")
    public DataSourceTransactionManager originReturnTransactionManager() {
        return new DataSourceTransactionManager(originReturnDataSource());
    }

    @Bean(name = "originReturnSqlSessionFactory")
    public SqlSessionFactory clusterSqlSessionFactory(@Qualifier("originReturnDataSource") DataSource originReturnDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(originReturnDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(OriginReturnDataSourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
