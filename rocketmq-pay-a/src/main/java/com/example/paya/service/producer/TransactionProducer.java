package com.example.paya.service.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.paya.service.provider
 * @ClassName TransactionProducer
 * @description MQ事务消息 - 生产者
 * @date created in 2021-07-27 14:46
 * @modified by
 */
@Component
public class TransactionProducer implements InitializingBean {

    @Autowired
    private TransactionListenerImpl transactionListenerImpl;

    private TransactionMQProducer producer;

    private ExecutorService executorService;

    private static final String NAMESERVER = "192.168.8.108:9876;192.168.8.240:9876;192.168.8.246:9876;192.168.8.247:9876";

    private static final String PRODUCER_GROUP_NAME = "tx_pay_producer_group_name";

    private TransactionProducer() {
        this.producer = new TransactionMQProducer(PRODUCER_GROUP_NAME);
        this.executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("PRODUCER_GROUP_NAME" + "-check-thread");
                return thread;
            }
        });
        this.producer.setExecutorService(executorService);
        this.producer.setNamesrvAddr(NAMESERVER);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.producer.setTransactionListener(transactionListenerImpl);
        start();
    }

    private void start() throws MQClientException {
        this.producer.start();
    }

    public void shutdown() {
        this.producer.shutdown();
    }

    public TransactionSendResult transactionSendResult(Message msg, Object obj) {
        TransactionSendResult transactionSendResult = null;
        try {
            transactionSendResult = this.producer.sendMessageInTransaction(msg, obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactionSendResult;
    }

}
