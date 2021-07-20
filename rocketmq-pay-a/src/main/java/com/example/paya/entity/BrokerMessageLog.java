package com.example.paya.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "`broker_message_log`")
public class BrokerMessageLog {
    @Id
    @Column(name = "`message_id`")
    private String messageId;

    @Column(name = "`message`")
    private String message;

    @Column(name = "`try_count`")
    private Integer tryCount;

    @Column(name = "`status`")
    private String status;

    @Column(name = "`next_retry`")
    private Date nextRetry;

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
}