package com.example.rocketmq.transaction;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.rocketmq.transaction
 * @ClassName TransactionListenerImpl
 * @description
 * @date created in 2021-07-26 14:13
 * @modified by
 */
public class TransactionListenerImpl implements TransactionListener {

    /**
     * 本地事务
     *
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        System.out.println("---执行本地事务--- ");
        String callArg = (String) arg;
        System.out.println("callArg: " + callArg);
        System.out.println("Message: " + msg);
        // tx.begin
        // 数据库的落库操作

        // tx.commit
        return LocalTransactionState.COMMIT_MESSAGE;
//        return LocalTransactionState.UNKNOW; // 测试-回调消息检查
    }

    /**
     * @param msg
     * @return
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        System.out.println("---回调消息检查--- " + msg);
        return LocalTransactionState.COMMIT_MESSAGE;
    }

}
