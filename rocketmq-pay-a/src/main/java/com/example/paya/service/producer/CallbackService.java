package com.example.paya.service.producer;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.example.paya.entity.MqConsumerLog;
import com.example.paya.entity.MqProducerLog;
import com.example.paya.mapping.db2.MqConsumerLogMapper;
import com.example.paya.mapping.db2.MqProducerLogMapper;
import com.example.paya.utils.FastJsonConvertUtil;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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

    @Autowired
    private MqProducerLogMapper mqProducerLogMapper;

    @Autowired
    private MqConsumerLogMapper MqConsumerLogMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public void sendOKMessage(String orderId, String userId) {

        // 发送给订单服务那边
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("orderId", orderId);
        params.put("status", "2");    //ok

        String keys = UUID.randomUUID().toString() + "$" + System.currentTimeMillis();
        Message message = new Message(CALLBACK_PAY_TOPIC, CALLBACK_PAY_TAGS, keys, Objects.requireNonNull(FastJsonConvertUtil.convertObjectToJSON(params)).getBytes());

        // 保证100%发送成功，持久化到数据库
        MqProducerLog mqProducerLog = new MqProducerLog()
                .setMessageId(UUID.randomUUID().toString().substring(0, 32))
                .setTopic(CALLBACK_PAY_TOPIC)
                .setKeys(keys)
                .setTags(CALLBACK_PAY_TAGS)
                .setMessageBody(FastJsonConvertUtil.convertObjectToJSON(params));

        MqConsumerLog mqConsumerLog = new MqConsumerLog();
        BeanUtils.copyProperties(mqProducerLog, mqConsumerLog);


        try {
            // 持久化入库
            mqProducerLogMapper.insert(mqProducerLog);
            MqConsumerLogMapper.insertSelective(mqConsumerLog);
            // 异步发送
            threadPoolTaskExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    SendResult ret = null;
                    ret = syncProducer.sendMessage(message);
                    if (ret.getSendStatus() == SendStatus.SEND_OK) {
                        mqProducerLogMapper.deleteByPrimaryKey(mqProducerLog.getMessageId());
                    } else {
                        // 业务逻辑：看要不要定时发送
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            // 记录发送失败的错误
            MqProducerLog mpl = new MqProducerLog()
                    .setMessageId(mqProducerLog.getMessageId())
                    .setException(e.getMessage());
            mqProducerLogMapper.updateByPrimaryKey(mpl);
        }

    }

}

