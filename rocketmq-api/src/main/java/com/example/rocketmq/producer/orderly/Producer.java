package com.example.rocketmq.producer.orderly;

import com.example.rocketmq.constants.Const;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.rocketmq.producer.orderly
 * @ClassName Producer
 * @description
 * @date created in 2021-07-29 10:17
 * @modified by
 */
public class Producer {

    public static void main(String[] args) throws MQClientException {

        String groupName = "test_orderly_consumer_name";

        DefaultMQProducer producer = new DefaultMQProducer(groupName);

        producer.setNamesrvAddr(Const.NAMESRV_ADDR_MASTER_SLAVE);

        producer.start();

        // 标记=1
        forSendMsg(producer, 1);
        // 标记=2
        forSendMsg(producer, 2);
        // 标记=3
        forSendMsg(producer, 3);

        // 关闭
        producer.shutdown();

    }

    private static void forSendMsg(DefaultMQProducer producer, int mark) {

        int num = 5;

        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        try {
            for (int i = 0; i < num; i++) {
                // 时间戳
                String body = dateStr + " Hello RocketMQ = " + i;
                // 参数： topic tag message
                Message message = new Message("test_order_topic", "TagA", "KEY" + i, body.getBytes());
                // 发送数据：如果使用顺序消费，则必须自己实现MessageQueueSelector, 保证消息进入同一个队列
                SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                        Integer id = (Integer) o;
                        System.err.println("id: " + id);
                        return list.get(id);
                    }
                    // 1 是队列下标
                }, mark);

                System.err.println(sendResult + ", body:" + body);
            }

        } catch (MQBrokerException | MQClientException | RemotingException | InterruptedException e) {
            e.printStackTrace();
            // 【Bug】Either re-interrupt this method or rethrow the "InterruptedException"
            Thread.currentThread().interrupt();
        }

    }

}
