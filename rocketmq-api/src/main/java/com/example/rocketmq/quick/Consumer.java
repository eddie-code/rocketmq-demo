package com.example.rocketmq.quick;

import com.example.rocketmq.constants.Const;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.rocketmq.quick
 * @ClassName Consumer
 * @description
 * @date created in 2021-07-02 11:57
 * @modified by
 */
public class Consumer {

    public static void main(String[] args) throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("temp_quick_consumer_name");

        consumer.setNamesrvAddr(Const.NAMESRV_ADDR_SINGLE);

        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

        consumer.subscribe("temp_quick_topic", "*");
//        consumer.subscribe("temp_quick_topic", "TagA");

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt me = list.get(0);
                try {
                    String topic = me.getTopic();
                    String tags = me.getTags();
                    String keys = me.getKeys();
                    String body = new String(me.getBody(), RemotingHelper.DEFAULT_CHARSET);
                    System.out.println("topic：" + topic + " tags: " + tags + " keys: " + keys + " body: " + body);

                    if (keys.equals("key1")) {
                        System.out.println("消息消费失败...");
                        int a = 1 / 0;
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    // 记录失败的次数
                    int reconsumeTimes = me.getReconsumeTimes();
                    System.out.println("reconsumeTimes: " + reconsumeTimes);
                    // 失败次数达到==3，就返回消费成功
                    if (reconsumeTimes == 3) {
                        // 日志...
                        // 做补偿机制
                        return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.println("consumer start!");

    }

}
