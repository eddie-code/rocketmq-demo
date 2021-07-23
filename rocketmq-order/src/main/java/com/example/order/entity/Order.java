package com.example.order.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author eddie
 */
@Data
@Accessors(chain = true)
@Table(name = "`t_order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 8196523499263987003L;

//    @Id
    @Column(name = "`order_id`")
    private String orderId;

    @Column(name = "`order_type`")
    private String orderType;

    @Column(name = "`city_id`")
    private String cityId;

    @Column(name = "`platform_id`")
    private String platformId;

    @Column(name = "`user_id`")
    private String userId;

    @Column(name = "`supplier_id`")
    private String supplierId;

    @Column(name = "`goods_id`")
    private String goodsId;

    @Column(name = "`order_status`")
    private String orderStatus;

    @Column(name = "`remark`")
    private String remark;

    @Column(name = "`create_by`")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    @Column(name = "`update_by`")
    private String updateBy;

    /**
     * 修改时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;
}