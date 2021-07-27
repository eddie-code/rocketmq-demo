package com.example.paya.web;

import com.example.paya.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.paya.web
 * @ClassName PayController
 * @description
 * @date created in 2021-07-27 14:08
 * @modified by
 */
@RestController
public class PayController {

    @Autowired
    private PayService payService;

    @RequestMapping("/pay")
    public String pay(@RequestParam("userId") String userId,
                      @RequestParam("orderId") String orderId,
                      @RequestParam("accountId") String accountId,
                      @RequestParam("money") double money) {
        return payService.payment(userId, orderId, accountId, money);
    }

}
