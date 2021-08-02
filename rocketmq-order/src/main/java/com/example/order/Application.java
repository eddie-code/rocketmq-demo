package com.example.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.order
 * @ClassName Application
 * @description
 * @date created in 2021-07-16 10:43
 * @modified by
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
