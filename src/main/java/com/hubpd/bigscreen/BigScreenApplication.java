package com.hubpd.bigscreen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 程序启动类
 * Created by ceek on 2018-08-07 23:13.
 */
@SpringBootApplication
//@MapperScan("com.hubpd.bigscreen.mapper")//将项目中对应的mapper类的路径加进来就可以了
public class BigScreenApplication {
    public static void main(String[] args) {
        SpringApplication.run(BigScreenApplication.class, args);
    }
}