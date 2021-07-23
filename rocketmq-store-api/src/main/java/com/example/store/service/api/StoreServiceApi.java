package com.example.store.service.api;

import java.util.Date;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.store.service.api
 * @ClassName StoreServiceApi
 * @description
 * @date created in 2021-07-22 10:30
 * @modified by
 */
public interface StoreServiceApi {

    /**
     * 查询对应的版本号
     *
     * @param supplierId 供应商id
     * @param goodsId    商品id
     * @return 0
     */
    int selectVersion(String supplierId, String goodsId);

    /**
     * 乐观锁的更新
     *
     * @param version    版本号
     * @param supplierId 供应商id
     * @param goodsId    商品id
     * @param updateBy   更新人
     * @param updateTime 更新时间
     * @return 0
     */
    int updateStoreCountByVersion(int version, String supplierId, String goodsId, String updateBy, Date updateTime);

    /**
     * 查询当前的库存
     *
     * @param supplierId 供应商id
     * @param goodsId    商品id
     * @return 0
     */
    int selectStoreCount(String supplierId, String goodsId);

}
