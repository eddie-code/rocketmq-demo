package com.example.paya.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "`mq_producer_log`")
public class MqProducerLog {
    /**
     * 消息唯一标识
     */
    @Id
    @Column(name = "`message_id`")
    private String messageId;

    /**
     * 主题
     */
    @Column(name = "`topic`")
    private String topic;

    /**
     * 标签
     */
    @Column(name = "`tags`")
    private String tags;

    /**
     * 消息体唯一标识
     */
    @Column(name = "`keys`")
    private String keys;

    /**
     * 消息体, json格式化
     */
    @Column(name = "`message_body`")
    private String messageBody;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     * 异常原因
     */
    @Column(name = "`exception`")
    private String exception;
}