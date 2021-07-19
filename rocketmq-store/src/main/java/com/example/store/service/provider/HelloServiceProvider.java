package com.example.store.service.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.example.store.service.api.HelloServiceApi;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.store.service.provider
 * @ClassName HelloServiceProvider
 * @description
 * @date created in 2021-07-19 11:44
 * @modified by
 */
@Service(
        version = "0.0.1",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class HelloServiceProvider implements HelloServiceApi {

    @Override
    public String sayHello(String name) {
        System.out.println("Name: " + name);
        return "Hello " + name;
    }
}
