package com.example.order.service.consumer;


import java.sql.Wrapper;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.example.order.constants.OrderStatus;
import com.example.order.entity.MqConsumerLog;
import com.example.order.mapping.db1.OrderMapper;
import com.example.order.mapping.db2.MqConsumerLogMapper;
import com.example.order.service.OrderService;
import com.example.order.utils.FastJsonConvertUtil;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.order.service.consumer
 * @ClassName OrderConsumer
 * @description 00：12：26
 * @date created in 2021-07-27 22:52
 * @modified by
 */
@Component
public class OrderConsumer {

    private DefaultMQPushConsumer consumer;

    public static final String CALLBACK_PAY_TOPIC = "callback_pay_topic";

    public static final String CALLBACK_PAY_TAGS = "callback_pay";

    private static final String NAMESERVER = "192.168.8.108:9876;192.168.8.240:9876;192.168.8.246:9876;192.168.8.247:9876";

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MqConsumerLogMapper mqConsumerLogMapper;

    public OrderConsumer() throws MQClientException {
        consumer = new DefaultMQPushConsumer("callback_pay_consumer_group");
        consumer.setConsumeThreadMin(10);
        consumer.setConsumeThreadMax(50);
        consumer.setNamesrvAddr(NAMESERVER);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.subscribe(CALLBACK_PAY_TOPIC, CALLBACK_PAY_TAGS);
        consumer.registerMessageListener(new MessageListenerConcurrently4Pay());
        consumer.start();
    }

    class MessageListenerConcurrently4Pay implements MessageListenerConcurrently {

        // 监听逻辑
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            MessageExt msg = msgs.get(0);
            MqConsumerLog mqConsumerLog = null;
            try {
                String topic = msg.getTopic();
                String msgBody = new String(msg.getBody(), "utf-8");
                String tags = msg.getTags();
                String keys = msg.getKeys();
                System.err.println("收到消息：" + "  topic :" + topic + "  ,tags : " + tags + "keys :" + keys + ", msg : " + msgBody);
                String orignMsgId = msg.getProperties().get(MessageConst.PROPERTY_ORIGIN_MESSAGE_ID);
                System.err.println("orignMsgId: " + orignMsgId);

                // 通过keys 进行去重表去重 或者使用redis进行去重???? --> 不需要

                // 获取消息
                Map<String, Object> body = FastJsonConvertUtil.convertJSONToObject(msgBody, Map.class);
                String orderId = (String) body.get("orderId");
                String userId = (String) body.get("userId");
                String status = (String) body.get("status");

                // 查询这条消息是否存在
                Example mclExp = new Example(MqConsumerLog.class);
                Example.Criteria criteria = mclExp.createCriteria();
                criteria.andEqualTo("topic", topic)
                        .andEqualTo("tags", tags)
                        .andEqualTo("keys", keys);
                mclExp.orderBy("createTime").desc();
                mqConsumerLog = mqConsumerLogMapper.selectOneByExample(mclExp);

                if (mqConsumerLog == null) {
                    System.err.println("mqConsumerLog表没有数据~");
                    return null;
                }

                // 判断消息状态
                // 状态：0未处理，1处理中，2处理失败， 3处理成功
                Integer mqStatus = mqConsumerLog.getStatus();
                if (mqStatus == 1 || mqStatus == 3) {
                    // 处理中或者处理成功都不需要重复处理(消息幂等性)
                    System.err.println("处理中或者处理成功都不需要重复处理(消息幂等性)");
                    return null;
                }

                // 处理业务逻辑
                Date currentTime = new Date();
                if (status.equals(OrderStatus.ORDER_PAYED.getValue())) {
                    // 00:14：21
                    int count = orderMapper.updateOrderStatus(orderId, status, "eddie", currentTime);
                    // 00:46:55
                    // 更新状态成功了，之后
                    if (count == 1) {
                        // 顺序消息相关 -- Start
                        System.err.println("userId: " + userId + " orderId: " + orderId);
                        orderService.sendOrderlyMessage4Pkg(userId, orderId);
                    }
                }

                // 使用乐观锁修改消息状态为成功，如果update失败表示并发修改
                int row = mqConsumerLogMapper.updatetryCountByVersion(mqConsumerLog.getVersion(),
                        "3",
                        mqConsumerLog.getTopic(),
                        mqConsumerLog.getTags(),
                        mqConsumerLog.getKeys(),
                        currentTime);

                if (row < 0) {
                    System.err.println("并发修改~~");
                } else {
                    System.err.println("消费成功：" + "  topic :" + topic + "  ,tags : " + tags + "keys :" + keys + ", msg : " + msgBody);

                }
            } catch (Exception e) {
                // 标记消息状态为处理失败，并累加消费次数
                e.printStackTrace();
                MqConsumerLog mclTry = new MqConsumerLog()
                        .setMessageId(mqConsumerLog.getMessageId())
                        .setStatus(2)
                        .setTryCount(mqConsumerLog.getTryCount() + 1)
                        .setException(e.getMessage())
                        .setVersion(mqConsumerLog.getVersion());
                mqConsumerLogMapper.updateByPrimaryKeySelective(mclTry);

                // 超过指定消费次数记录日志，人工干预
                int reconsumeTimes = msg.getReconsumeTimes();
                System.err.println("reconsumeTimes: " + reconsumeTimes);
                if (reconsumeTimes == 3) {
                    // 日志...
                    // 做补偿机制
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }
}
