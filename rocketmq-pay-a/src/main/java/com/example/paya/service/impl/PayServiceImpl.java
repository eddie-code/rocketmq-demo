package com.example.paya.service.impl;

import com.example.paya.entity.CustomerAccount;
import com.example.paya.mapping.db1.CustomerAccountMapper;
import com.example.paya.service.PayService;
import com.example.paya.service.producer.CallbackService;
import com.example.paya.service.producer.TransactionProducer;
import com.example.paya.utils.FastJsonConvertUtil;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.paya.service.impl
 * @ClassName PayServiceImpl
 * @description
 * @date created in 2021-07-27 14:10
 * @modified by
 */
@Service
public class PayServiceImpl implements PayService {

    public static final String TX_PAY_TOPIC = "tx_pay_topic";

    public static final String TX_PAY_TAGS = "pay";

    @Autowired
    private CustomerAccountMapper customerAccountMapper;

    @Autowired
    private TransactionProducer transactionProducer;

    @Autowired
    private CallbackService callbackService;

    /**
     * tips: 有一定概率，会出现并发，都去查询账户余额，都去减钱，都去发送消息
     * 保障：
     * 1. 一次性的token去重 （防止自身重覆提交）
     * 2. redis分布式锁：防止同一时间同一账户提交操作 （低概率）
     * - 如果在大型电商平台，出现redis分布式锁获取失败，也必需让代码走下去
     * 3. 获取分布式锁失败了，就需要依靠数据库的乐观锁去重
     *
     * @param userId    用户id
     * @param orderId   订单id
     * @param accountId 账户id
     * @param money     钱
     * @return
     */
    @Override
    public String payment(String userId, String orderId, String accountId, double money) {
        String paymentRet = "";
        try {
            // 唯一性：最开始有一步token验证操作，只能使用一次的token (重覆提单问题)

            // Double 转换 BigDecimal
            BigDecimal payMoney = BigDecimal.valueOf(money);

            // -------------------------------加乐观锁开始（未实现）-------------------------------
            // 1. 先查询账户是否有余额
            CustomerAccount old = customerAccountMapper.selectByPrimaryKey(accountId);

            //  1.1 准备有用历史账户数据
            BigDecimal currentBalance = old.getCurrentBalance();
            int currentVersion = old.getVersion();

            // ---------------------------------------------------------
            // 要对大概率时间进行提前预判（小概率时间我们做放过）
            //
            // 技术出发：
            // redis去重，分布式锁
            // 数据库乐观锁去重
            //
            // 业务出发：
            // 当前一个用户账户只允许一个线程（一个应用端访问）
            // ---------------------------------------------------------

            // 2. 当前的余额
            System.out.println("-----减前：" + payMoney.toString());
            // 做扣款操作的时候： 获得分布式锁，看一下能否获得
            // TODO 自己加Redis分布式锁
            BigDecimal newBalance = currentBalance.subtract(payMoney);
            System.out.println("-----减后：" + newBalance.toString());

            // -------------------------------加乐观锁结束 - 释放（未实现）-------------------------------

            if (newBalance.doubleValue() > 0) {
                // 2.1 组装消息 -->  发送MQ
                String keys = UUID.randomUUID().toString() + "$" + System.currentTimeMillis();
                Map<String, Object> params = new HashMap<>();
                params.put("userId", userId);
                params.put("orderId", orderId);
                params.put("accountId", accountId);
                params.put("money", money); // 100

                Message message = new Message(TX_PAY_TOPIC, TX_PAY_TAGS, keys, FastJsonConvertUtil.convertObjectToJSON(params).getBytes());

                // 3. 执行本地事务
                // 可能使用到的参数
                params.put("payMoney", payMoney);
                params.put("newBalance", newBalance);
                params.put("currentVersion", currentVersion);

                //	同步阻塞
                CountDownLatch countDownLatch = new CountDownLatch(1);
                params.put("currentCountDown", countDownLatch);
                // 消息发送并且本地的事务执行（并行操作）
                TransactionSendResult transactionSendResult = transactionProducer.transactionSendResult(message, params);

                countDownLatch.await();

                if (transactionSendResult.getSendStatus() == SendStatus.SEND_OK && transactionSendResult.getLocalTransactionState() == LocalTransactionState.COMMIT_MESSAGE) {
                    //	回调 order 通知支付成功消息
                    callbackService.sendOKMessage(orderId, userId);
                    paymentRet = "支付成功!";
                } else {
                    paymentRet = "支付失败!";
                }

            } else {
                paymentRet = "余额不足！";
            }

        } catch (Exception e) {
            e.printStackTrace();
            paymentRet = "支付失败!";
        }
        return paymentRet;
    }

}
