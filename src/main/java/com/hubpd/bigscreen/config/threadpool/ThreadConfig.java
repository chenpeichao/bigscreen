package com.hubpd.bigscreen.config.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步线程配置类
 *
 * @author cpc
 * @create 2018-08-17 16:35
 **/
@Configuration
@ComponentScan("com.hubpd.bigscreen.service.common.impl")
@EnableAsync
public class ThreadConfig {
    /**
     * 执行需要依赖线程池，这里就来配置一个线程池
     *
     * @return
     */
    @Bean
    public Executor getExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(50);
//        executor.setQueueCapacity(250);
        executor.initialize();
        return executor;
    }
}
