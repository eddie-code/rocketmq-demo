package com.example.order.service;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.order.service
 * @ClassName OrderService
 * @description
 * @date created in 2021-07-22 9:38
 * @modified by
 */
public interface OrderService {

    boolean createOrder(String cityId, String platformId, String userId, String supplierId, String goodsId);

    void sendOrderlyMessage4Pkg(String userId, String orderId);
}
