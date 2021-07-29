package com.example.paya.service.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.paya.service.producer
 * @ClassName SyncProducer
 * @description
 * @date created in 2021-07-27 22:18
 * @modified by
 */
@Component
public class SyncProducer {

    private DefaultMQProducer producer;

    private static final String NAMESERVER = "192.168.8.108:9876;192.168.8.240:9876;192.168.8.246:9876;192.168.8.247:9876";

    private static final String PRODUCER_GROUP_NAME = "callback_pay_producer_group_name";

    private SyncProducer() {
        this.producer = new DefaultMQProducer(PRODUCER_GROUP_NAME);
        this.producer.setNamesrvAddr(NAMESERVER);
        // 重试三次
        this.producer.setRetryTimesWhenSendFailed(3);
        start();
    }

    public void start() {
        try {
            this.producer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

    public SendResult sendMessage(Message message) {
        SendResult sendResult = null;
        try {
            // 同步发送
            sendResult = this.producer.send(message);
            // 异步发送
//            this.producer.send(message, new SendCallback() {
//                @Override
//                public void onSuccess(SendResult sendResult) {
//                    System.err.println("msgId: " + sendResult.getMsgId() + ", status: " + sendResult.getSendStatus());
//                    //可以处理相应的业务
//                }
//
//                @Override
//                public void onException(Throwable throwable) {
//                    throwable.printStackTrace();
//                    System.err.println("------发送失败");
//                    //可以处理相应的业务
//                }
//            });
        } catch (MQClientException e) {
            e.printStackTrace();
        } catch (RemotingException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (MQBrokerException e) {
            e.printStackTrace();
        }
        return sendResult;
    }

    public void shutdown() {
        this.producer.shutdown();
    }
}
