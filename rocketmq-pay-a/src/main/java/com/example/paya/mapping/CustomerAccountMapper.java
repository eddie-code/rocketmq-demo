package com.example.paya.mapping;

import com.example.paya.entity.CustomerAccount;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.Date;

public interface CustomerAccountMapper extends Mapper<CustomerAccount> {

    /**
     * @param accountId
     * @param newBalance
     * @param currentVersion
     * @param currentTime
     * @return
     */
    int updateBalance(@Param("accountId") String accountId,
                      @Param("newBalance") BigDecimal newBalance,
                      @Param("version") Integer currentVersion,
                      @Param("updateTime") Date currentTime);

}