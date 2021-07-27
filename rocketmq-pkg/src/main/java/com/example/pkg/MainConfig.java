package com.example.pkg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.order
 * @ClassName MainConfig
 * @description
 * @date created in 2021-07-16 10:44
 * @modified by
 */
@Configuration
@ComponentScan(basePackages = {"com.example.pkg.*"})
@MapperScan(basePackages = "com.example.pkg.mapping")
public class MainConfig {



}
