package com.example.store.config.mybatis;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.order.config.mybatis
 * @ClassName MybatisMapperScanerConfig
 * @description
 * @date created in 2021-07-16 14:55
 * @modified by
 */
//@Configuration
//@AutoConfigureAfter(MybatisDataSourceConfig.class)
public class MybatisMapperScanerConfig {

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("com.example.store.mapper");
        return mapperScannerConfigurer;
    }

}
