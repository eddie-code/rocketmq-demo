package com.example.order.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "`mq_consumer_log`")
public class MqConsumerLog {
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
     * 重试次数
     */
    @Column(name = "`try_count`")
    private Integer tryCount;

    /**
     * 状态：0未处理，1处理中，2处理失败， 3处理成功
     */
    @Column(name = "`status`")
    private Integer status;

    /**
     * 乐观锁
     */
    @Column(name = "`version`")
    private Integer version;

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