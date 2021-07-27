package com.example.paya.service;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.paya.service.impl
 * @ClassName PayService
 * @description
 * @date created in 2021-07-27 14:09
 * @modified by
 */
public interface PayService {

    /**
     * 付款
     *
     * @param userId
     * @param orderId
     * @param accountId
     * @param money
     * @return
     */
    String payment(String userId, String orderId, String accountId, double money);

}
