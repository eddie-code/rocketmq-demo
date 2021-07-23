package com.example.store.service.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.example.store.mapping.StoreMapper;
import com.example.store.service.api.StoreServiceApi;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @author eddie.lee
 * @ProjectName rocketmq-demo
 * @Package com.example.store.service.provider
 * @ClassName StoreServiceProvider
 * @description
 * @date created in 2021-07-22 10:34
 * @modified by
 */
@Service(
        version = "0.0.1",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class StoreServiceProvider implements StoreServiceApi {

    @Autowired
    private StoreMapper storeMapper;

    @Override
    public int selectVersion(String supplierId, String goodsId) {
        return storeMapper.selectVersion(supplierId, goodsId);
    }

    @Override
    public int updateStoreCountByVersion(int version, String supplierId, String goodsId, String updateBy, Date updateTime) {
        return storeMapper.updateStoreCountByVersion(version, supplierId, goodsId, updateBy, updateTime);
    }

    @Override
    public int selectStoreCount(String supplierId, String goodsId) {
        return storeMapper.selectStoreCount(supplierId, goodsId);
    }

}
