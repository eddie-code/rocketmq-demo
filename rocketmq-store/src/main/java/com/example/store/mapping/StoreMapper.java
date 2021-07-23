package com.example.store.mapping;

import com.example.store.entity.Store;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

public interface StoreMapper extends Mapper<Store> {

    int selectVersion(@Param("supplierId") String supplierId, @Param("goodsId") String goodsId);

    int updateStoreCountByVersion(@Param("version") int version,
                                  @Param("supplierId") String supplierId,
                                  @Param("goodsId") String goodsId,
                                  @Param("updateBy") String updateBy,
                                  @Param("updateTime") Date updateTime);

    int selectStoreCount(@Param("supplierId") String supplierId, @Param("goodsId") String goodsId);
}