package com.example.order.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.example.order.constants.OrderStatus;
import com.example.order.entity.Order;
import com.example.order.mapping.OrderMapper;
import com.example.order.service.OrderService;
import com.example.store.service.api.StoreServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.UUID;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.order.service.impl
 * @ClassName OrderServiceimpl
 * @description
 * @date created in 2021-07-22 9:39
 * @modified by
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Reference(
            // 服务版本，与服务提供者的版本一致
            version = "0.0.1",
            application = "${dubbo.application.id}",
            interfaceName = "com.example.store.service.StoreServiceApi",
            check = false,
            timeout = 3000,
            // 读请求允许重试3次，写请求不要进行重试
            retries = 0
    )
    private StoreServiceApi storeServiceApi;

    @Override
    public boolean createOrder(String cityId, String platformId, String userId, String supplierId, String goodsId) {
        boolean flag = true;
        try {
            // 1. 创建订单
            Date currentTime = new Date();
            Order order = new Order()
                    .setOrderId(UUID.randomUUID().toString().substring(0, 32))
                    .setOrderType("1")
                    .setCityId(cityId)
                    .setPlatformId(platformId)
                    .setUserId(userId)
                    .setSupplierId(supplierId)
                    .setGoodsId(goodsId)
                    .setOrderStatus(OrderStatus.ORDER_CREATED.getValue())
                    .setRemark("")
                    .setCreateBy("eddie")
                    .setCreateTime(currentTime)
                    .setUpdateBy("eddie")
                    .setUpdateTime(currentTime);

            // 2. 查询version版本号
            int currentVersion = storeServiceApi.selectVersion(supplierId, goodsId);

            // 3. 根据查询version版本号和其他条件，乐观锁的更新
            int updateRetCount = storeServiceApi.updateStoreCountByVersion(currentVersion, supplierId, goodsId, "eddie", currentTime);

            // 4. 根据更新乐观锁的返回值，判断是否成功
            if (updateRetCount == 1) {
                // 如果出现SQL异常 入库失败, 那么要对 库存的数量 和版本号进行回滚操作
                orderMapper.insertSelective(order);
                // 没有更新成功 1.高并发时乐观锁生效 2.库存不足
            } else if (updateRetCount == 0) {
                // 下单标识失败
                flag = false;
                // 查询当前的库存
                int currentStoreCount = storeServiceApi.selectStoreCount(supplierId, goodsId);
                if (currentStoreCount == 0) {
                    // {flag:false, messageCode: 001, message: 当前库存不足}
                    System.out.println("当前库存不足...");
                } else {
                    // {flag:false, messageCode: 002, message: 乐观锁生效}
                    System.out.println("乐观锁生效...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 具体捕获的异常是什么异常
            flag = false;
            // 5. 如果更新库存失败, 需要捕获的异常做回滚操作
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return flag;
    }

    // 01：10：50

}
