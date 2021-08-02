package com.example.order.mapping.db2;

import com.example.order.entity.MqConsumerLog;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

public interface MqConsumerLogMapper extends Mapper<MqConsumerLog> {

    int updatetryCountByVersion(@Param("version") int version,
                                  @Param("status") String status,
                                  @Param("topic") String topic,
                                  @Param("tags") String tags,
                                  @Param("keys") String keys,
                                  @Param("updateTime") Date updateTime);

}