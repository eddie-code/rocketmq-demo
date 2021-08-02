package com.example.paya.mapping.db2;

import com.example.paya.entity.MqConsumerLog;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface MqConsumerLogMapper extends Mapper<MqConsumerLog> {
}