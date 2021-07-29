package com.example.rocketmq.producer.orderly;

import com.example.rocketmq.constants.Const;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.rocketmq.producer.orderly
 * @ClassName Consumer
 * @description 顺序消息消费
 * @date created in 2021-07-29 10:10
 * @modified by
 */
public class Consumer {

    public static void main(String[] args) throws MQClientException {

        Consumer c1 = new Consumer();

    }

    public Consumer() throws MQClientException {

        String groupName = "test_orderly_consumer_name";

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(Const.NAMESRV_ADDR_MASTER_SLAVE);

        /**
         * 设置 consumer 第一次启动是从队列头部开始消费还是队列尾巴开始消费<br>
         * 如果非第一次启动，那么按照上次消费的位置继续消费
         */
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        // 订阅的主题，以及过滤标签内容
        consumer.subscribe("test_order_topic", "TagA");
        // 注册监听
        consumer.registerMessageListener(new Listener());
        consumer.start();
        System.err.println("Consumer Started...");

    }

    /**
     * MessageListenerOrderly: 一个线程监听一个队列
     */
    class Listener implements MessageListenerOrderly {

        private Random random = new Random();

        @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
            // 设置自动提交
//            consumeOrderlyContext.setAutoCommit(true);

            for (MessageExt me : list) {
                System.err.println(me + ", content: " + new String(me.getBody()));
                try {
                    // 模拟业务逻辑处理...
                    TimeUnit.SECONDS.sleep(random.nextInt(4) + 1);
                } catch (Exception e) {
                    e.printStackTrace();
//                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }
            return ConsumeOrderlyStatus.SUCCESS;
        }
    }

}
