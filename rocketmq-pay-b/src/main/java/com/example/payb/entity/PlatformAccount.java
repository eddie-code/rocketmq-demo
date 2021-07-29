package com.example.payb.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "`t_platform_account`")
public class PlatformAccount implements Serializable {

    private static final long serialVersionUID = 6736434177179025144L;

    @Id
    @Column(name = "`account_id`")
    private String accountId;

    @Column(name = "`account_no`")
    private String accountNo;

    @Column(name = "`date_time`")
    private Date dateTime;

    @Column(name = "`current_balance`")
    private BigDecimal currentBalance;

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
}