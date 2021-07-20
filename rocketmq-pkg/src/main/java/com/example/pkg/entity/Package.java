package com.example.pkg.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "`t_package`")
public class Package {
    @Id
    @Column(name = "`package_id`")
    private String packageId;

    @Column(name = "`order_id`")
    private String orderId;

    @Column(name = "`supplier_id`")
    private String supplierId;

    @Column(name = "`address_id`")
    private String addressId;

    @Column(name = "`remark`")
    private String remark;

    @Column(name = "`package_status`")
    private String packageStatus;

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