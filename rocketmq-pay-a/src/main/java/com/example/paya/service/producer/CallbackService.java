package com.example.paya.service.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.example.paya.utils.FastJsonConvertUtil;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.paya.service.producer
 * @ClassName CallbackService
 * @description
 * @date created in 2021-07-27 22:17
 * @modified by
 */
@Service
public class CallbackService {

    public static final String CALLBACK_PAY_TOPIC = "callback_pay_topic";

    public static final String CALLBACK_PAY_TAGS = "callback_pay";

//    private static final String NAMESERVER = "192.168.8.108:9876;192.168.8.240:9876;192.168.8.246:9876;192.168.8.247:9876";

    @Autowired
    private SyncProducer syncProducer;

    public void sendOKMessage(String orderId, String userId) {

        // 发送给订单服务那边
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("orderId", orderId);
        params.put("status", "2");    //ok

        String keys = UUID.randomUUID().toString() + "$" + System.currentTimeMillis();
        Message message = new Message(CALLBACK_PAY_TOPIC, CALLBACK_PAY_TAGS, keys, FastJsonConvertUtil.convertObjectToJSON(params).getBytes());

        // 同步发送
        SendResult ret = syncProducer.sendMessage(message);

        // 100%投递： 如若出现失败，可以通过返回值，修改人工参与。  如果使用异步发送就需要 BrokerMessageLog 这张表
    }

}

