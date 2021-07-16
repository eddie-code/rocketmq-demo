package com.example.order.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.order.web
 * @ClassName IndexController
 * @description
 * @date created in 2021-07-16 14:20
 * @modified by
 */
@RestController
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        System.out.println("==========");
        return "index";
    }

}
