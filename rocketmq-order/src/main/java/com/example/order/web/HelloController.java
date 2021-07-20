package com.example.order.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.store.service.api.HelloServiceApi;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.order.web
 * @ClassName HelloController
 * @description
 * @date created in 2021-07-19 13:43
 * @modified by
 */
@RestController
public class HelloController {

    /**
     * @Reference 用在消费端，表明使用的是服务端的什么服务
     */
    @Reference(
            // 服务版本，与服务提供者的版本一致
            version = "0.0.1",
            application = "${dubbo.application.id}",
            interfaceName = "com.example.store.service.HelloServiceApi",
            check = false,
            timeout = 3000,
            // 读请求允许重试3次，写请求不要进行重试
            retries = 0
    )
    private HelloServiceApi helloServiceApi;

    @RequestMapping("/hello")
    public String hello(@RequestParam String name) {
        return helloServiceApi.sayHello(name);
    }

}
