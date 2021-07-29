/*
 Navicat Premium Data Transfer

 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 192.168.8.100:61337
 Source Schema         : broker_message

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 20/07/2021 15:38:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for broker_message_log
-- ----------------------------
DROP TABLE IF EXISTS `broker_message_log`;
CREATE TABLE `broker_message_log` (
  `message_id` varchar(32) NOT NULL COMMENT '消息唯一标识',
  `message` varchar(400) DEFAULT NULL COMMENT '消息体, json格式化',
  `try_count` int(5) DEFAULT NULL COMMENT '重试次数',
  `status` varchar(10) DEFAULT NULL COMMENT '状态: 0投递中 1投递成功 2投递失败 3已消费',
  `next_retry` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '下一次重试时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of broker_message_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
