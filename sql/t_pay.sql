/*
 Navicat Premium Data Transfer

 Source Server         : localDocker-MySQL8
 Source Server Type    : MySQL
 Source Server Version : 80021 (8.0.21)
 Source Host           : localhost:8000
 Source Schema         : sgg_cloud_db2024

 Target Server Type    : MySQL
 Target Server Version : 80021 (8.0.21)
 File Encoding         : 65001

 Date: 25/08/2024 11:24:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_pay
-- ----------------------------
DROP TABLE IF EXISTS `t_pay`;
CREATE TABLE `t_pay`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT,
  `pay_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付流水号',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单流水号',
  `user_id` int NULL DEFAULT 1 COMMENT '用户账号ID',
  `amount` decimal(8, 2) NOT NULL DEFAULT 9.90 COMMENT '交易金额',
  `deleted` tinyint UNSIGNED NOT NULL DEFAULT 0 COMMENT '删除标志，默认0不删除，1删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付交易表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_pay
-- ----------------------------
INSERT INTO `t_pay` VALUES (1, 'pay17203699', '6544bafb424a', 1, 1000.00, 0, '2024-04-19 22:21:19', '2024-04-23 16:50:08');
INSERT INTO `t_pay` VALUES (2, 'paycbb34update', 'rrrrrr', 11, 119.11, 0, '2024-04-22 19:46:25', '2024-04-22 19:46:25');
INSERT INTO `t_pay` VALUES (3, 'paycbb34update', 'rrrrrr', 11, 119.11, 0, '2024-04-22 19:41:24', '2024-04-22 19:41:24');
INSERT INTO `t_pay` VALUES (4, 'UellRo520y', 'wpp2Vosoe3', 87, 630.17, 126, '2000-08-12 21:07:51', '2021-03-27 11:34:57');
INSERT INTO `t_pay` VALUES (5, 'qZXYwNXVjM', 'mHkLiyQYCM', 572, 915.41, 64, '2016-08-22 01:52:46', '2000-07-03 13:56:00');
INSERT INTO `t_pay` VALUES (6, 'LSIh4mHpf2', '3iKua3xgNV', 934, 23.25, 63, '2021-04-27 00:44:42', '2004-06-27 13:16:18');
INSERT INTO `t_pay` VALUES (7, '2TlegUSDTu', 'dOv60bmclj', 273, 568.60, 61, '2004-06-13 22:41:26', '2001-05-10 13:32:41');
INSERT INTO `t_pay` VALUES (8, 'lqwnAWyWm5', 'Vj4PwyfPby', 760, 55.24, 28, '2017-02-05 08:58:36', '2013-12-11 08:38:19');
INSERT INTO `t_pay` VALUES (9, 'eocQi8XMcQ', 'U1AI5FLjX7', 186, 560.79, 174, '2002-04-28 11:22:23', '2021-03-25 02:33:48');
INSERT INTO `t_pay` VALUES (10, '1E0HoON5Fy', 'yk7Qvh1RI2', 596, 940.74, 243, '2000-03-29 06:17:15', '2020-05-03 01:45:11');
INSERT INTO `t_pay` VALUES (11, 'HkdFZ9rWFW', 'H2kws7fxCu', 646, 411.27, 103, '2022-02-23 12:34:51', '2020-07-04 01:56:38');
INSERT INTO `t_pay` VALUES (12, 'x8iObMpYKV', 'L4m6INGTVV', 888, 335.24, 197, '2012-04-25 09:31:15', '2018-02-03 23:44:24');
INSERT INTO `t_pay` VALUES (13, 'iTNbz7mQTx', 'P8vaMBfUjO', 510, 104.63, 172, '2023-02-04 20:27:42', '2023-04-28 16:40:04');

SET FOREIGN_KEY_CHECKS = 1;
