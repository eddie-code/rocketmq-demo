package com.example.order.web;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.order.web
 * @ClassName OrderController
 * @description
 * @date created in 2021-07-21 9:24
 * @modified by
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @HystrixCommand(
            commandKey = "/createOrder",
            fallbackMethod = "createOrderFallbackMethod",
            commandProperties = {
                    @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
            }
    )
    @RequestMapping("/createOrder/v1")
    public String createOrder(@RequestParam("cityId") String cityId,
                              @RequestParam("platformId") String platformId,
                              @RequestParam("userId") String userId,
                              @RequestParam("suppliedId") String suppliedId,
                              @RequestParam("goodsId") String goodsId) throws InterruptedException {
        Thread.sleep(5000);
        return "下单成功！";
    }

    public String createOrderFallbackMethod(@RequestParam("cityId") String cityId,
                                            @RequestParam("platformId") String platformId,
                                            @RequestParam("userId") String userId,
                                            @RequestParam("suppliedId") String suppliedId,
                                            @RequestParam("goodsId") String goodsId) {
        System.out.println("---超时降级策略执行----");
        return "Hystrix timeout!";
    }

}
