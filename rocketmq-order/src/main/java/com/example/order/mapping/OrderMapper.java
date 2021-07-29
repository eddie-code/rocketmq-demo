package com.example.order.mapping;

import com.example.order.entity.Order;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

@Repository
public interface OrderMapper extends Mapper<Order> {

    int updateOrderStatus(@Param("orderId") String orderId,
                          @Param("orderStatus") String status,
                          @Param("updateBy") String admin,
                          @Param("updateTime") Date currentTime);

}