package com.example.order.web;

import com.example.order.service.OrderService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderService orderService;

//    hystrix断路器线程池方式 - 超时降级
//    @HystrixCommand(
//            commandKey = "/createOrder",
//            fallbackMethod = "createOrderFallbackMethod4Timeout",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.timeout.enabled", value = "true"),
//                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
//            }
//    )

//    hystrix断路器线程池方式 - 限流 - 线程池方式
//    @HystrixCommand(
//            commandKey = "/createOrder",
//            commandProperties = {
//                    @HystrixProperty(name="execution.isolation.strategy", value="THREAD")
//            },
//            threadPoolKey = "createOrderThreadPool",
//            threadPoolProperties = {
//                    // 并发执行的最大线程数，默认10
//                    @HystrixProperty(name = "coreSize", value = "10"),
//                    // BlockingQueue的最大队列数，默认值-1
//                    @HystrixProperty(name = "maxQueueSize", value = "20000"),
//                    // 即使maxQueueSize没有达到，达到queueSizeRejectionThreshold该值后，请求也会被拒绝，默认值5
//                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "30")
//            },
//            fallbackMethod = "createOrderFallbackMethod4Thread"
//    )

    //	hystrix断路器线程池方式 - 限流 - 信号量方式
//    @HystrixCommand(
//            commandKey = "createOrder",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
//                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "3")
//            },
//            fallbackMethod = "createOrderFallbackMethod4semaphore"
//    )
    @RequestMapping("/createOrder/v1")
    public String createOrder(@RequestParam("cityId") String cityId,
                              @RequestParam("platformId") String platformId,
                              @RequestParam("userId") String userId,
                              @RequestParam("supplierId") String supplierId,
                              @RequestParam("goodsId") String goodsId) throws InterruptedException {
//        Thread.sleep(5000);
        return orderService.createOrder(cityId, platformId, userId, supplierId, goodsId) ? "下单成功!" : "下单失败!";
    }

    public String createOrderFallbackMethod4Timeout(@RequestParam("cityId") String cityId,
                                                    @RequestParam("platformId") String platformId,
                                                    @RequestParam("userId") String userId,
                                                    @RequestParam("suppliedId") String suppliedId,
                                                    @RequestParam("goodsId") String goodsId) {
        System.out.println("---超时降级策略执行----");
        return "Hystrix timeout!";
    }

    public String createOrderFallbackMethod4Thread(@RequestParam("cityId") String cityId,
                                                   @RequestParam("platformId") String platformId,
                                                   @RequestParam("userId") String userId,
                                                   @RequestParam("suppliedId") String suppliedId,
                                                   @RequestParam("goodsId") String goodsId) throws Exception {
        System.err.println("---线程池限流降级策略执行---");
        return "hysrtix threadpool !";
    }

    public String createOrderFallbackMethod4semaphore(@RequestParam("cityId") String cityId,
                                                      @RequestParam("platformId") String platformId,
                                                      @RequestParam("userId") String userId,
                                                      @RequestParam("suppliedId") String suppliedId,
                                                      @RequestParam("goodsId") String goodsId) throws Exception {
        System.err.println("-------信号量限流降级策略执行------------");
        return "hysrtix semaphore !";
    }

}
