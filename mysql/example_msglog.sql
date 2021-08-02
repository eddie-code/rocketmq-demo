/*
 Navicat Premium Data Transfer

 Source Server         : EDDIE-RocketMQ_本地消息表_100%投递
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : 192.168.8.100:61337
 Source Schema         : example_msglog

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 02/08/2021 15:30:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mq_consumer_log
-- ----------------------------
DROP TABLE IF EXISTS `mq_consumer_log`;
CREATE TABLE `mq_consumer_log`  (
  `message_id` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '消息唯一标识',
  `topic` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '主题',
  `tags` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '标签',
  `keys` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '消息体唯一标识',
  `message_body` varchar(400) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '消息体, json格式化',
  `try_count` int(5) NULL DEFAULT 0 COMMENT '重试次数',
  `exception` text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL COMMENT '异常原因',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态：0未处理，1处理中，2处理失败， 3处理成功',
  `version` int(10) NULL DEFAULT 0 COMMENT '乐观锁',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mq_consumer_log
-- ----------------------------

-- ----------------------------
-- Table structure for mq_producer_log
-- ----------------------------
DROP TABLE IF EXISTS `mq_producer_log`;
CREATE TABLE `mq_producer_log`  (
  `message_id` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '消息唯一标识',
  `topic` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '主题',
  `tags` varchar(50) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '标签',
  `keys` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '消息体唯一标识',
  `message_body` varchar(400) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '消息体, json格式化',
  `exception` text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL COMMENT '异常原因',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mq_producer_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
