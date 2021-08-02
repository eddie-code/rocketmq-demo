package com.example.paya.config.database;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.paya.config.database
 * @ClassName AsynCommonConfig
 * @description^ 异步线程发送消息Bean
 * @date created in 2021-07-30 20:16
 * @modified by
 */
public class AsynProducerConfig {

    @Bean
    public ThreadPoolTaskExecutor getThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("Pool-A");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
