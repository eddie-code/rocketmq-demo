package com.example.rocketmq.quick;

import com.example.rocketmq.constants.Const;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.rocketmq.quick
 * @ClassName Producer
 * @description
 * @date created in 2021-07-02 11:57
 * @modified by
 */
public class Producer {

    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {

        DefaultMQProducer producer = new DefaultMQProducer("temp_quick_producer_name");

        producer.setNamesrvAddr(Const.NAMESRV_ADDR_SINGLE);
//        producer.setCreateTopicKey("AUTO_CREATE_TOPIC_KEY");
//        producer.setVipChannelEnabled(false);

        producer.start();

        // 1. 创建消息
        for (int i = 0; i < 10; i++) {
            Message message = new Message("temp_quick_topic",
                    "TagA", // 标签
                    "key" + i, // 用户自定义的key, 唯一的标识
                    ("您好，阿里巴巴 RocketMQ" + i).getBytes() // 消息内容实体 （byte[]）
            );
            // 2. 发送消息
            SendResult sendResult = producer.send(message);
            System.out.println("消息发出：" + sendResult);
        }

        // 3. 关闭
        producer.shutdown();

    }

}
