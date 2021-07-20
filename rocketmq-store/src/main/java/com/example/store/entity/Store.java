package com.example.store.entity;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@Table(name = "`t_store`")
public class Store {
    @Id
    @Column(name = "`store_id`")
    private String storeId;

    @Column(name = "`goods_id`")
    private String goodsId;

    @Column(name = "`supplier_id`")
    private String supplierId;

    @Column(name = "`goods_name`")
    private String goodsName;

    @Column(name = "`store_count`")
    private Integer storeCount;

    @Column(name = "`version`")
    private Integer version;

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