/*
 Navicat Premium Data Transfer

 Source Server         : home
 Source Server Type    : MySQL
 Source Server Version : 80401 (8.4.1)
 Source Host           : home.overthinker.top:30970
 Source Schema         : easybilibili

 Target Server Type    : MySQL
 Target Server Version : 80401 (8.4.1)
 File Encoding         : 65001

 Date: 25/02/2025 02:10:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for QRTZ_BLOB_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
CREATE TABLE `QRTZ_BLOB_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_BLOB_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CALENDARS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
CREATE TABLE `QRTZ_CALENDARS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_CALENDARS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_CRON_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
CREATE TABLE `QRTZ_CRON_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CRON_EXPRESSION` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_CRON_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_FIRED_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
CREATE TABLE `QRTZ_FIRED_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `FIRED_TIME` bigint NOT NULL,
  `SCHED_TIME` bigint NOT NULL,
  `PRIORITY` int NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_FIRED_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_JOB_DETAILS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
CREATE TABLE `QRTZ_JOB_DETAILS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_JOB_DETAILS
-- ----------------------------
INSERT INTO `QRTZ_JOB_DETAILS` VALUES ('quartzScheduler', 'refreshTheCache', 'DEFAULT', '任务描述：用于每五分钟刷新一次常用数据缓存', 'xyz.kuailemao.quartz.RefreshTheCache', '1', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000010770800000010000000007800);

-- ----------------------------
-- Table structure for QRTZ_LOCKS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_LOCKS`;
CREATE TABLE `QRTZ_LOCKS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_LOCKS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_PAUSED_TRIGGER_GRPS
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SCHEDULER_STATE
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
CREATE TABLE `QRTZ_SCHEDULER_STATE`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `LAST_CHECKIN_TIME` bigint NOT NULL,
  `CHECKIN_INTERVAL` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_SCHEDULER_STATE
-- ----------------------------

-- ----------------------------
-- Table structure for QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `REPEAT_COUNT` bigint NOT NULL,
  `REPEAT_INTERVAL` bigint NOT NULL,
  `TIMES_TRIGGERED` bigint NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_SIMPLE_TRIGGERS
-- ----------------------------
INSERT INTO `QRTZ_SIMPLE_TRIGGERS` VALUES ('quartzScheduler', '6da64b5bd2ee-161d3704-6bee-45a1-8732-ed05422f5c61', 'DEFAULT', -1, 300000, 16389);

-- ----------------------------
-- Table structure for QRTZ_SIMPROP_TRIGGERS
-- ----------------------------
DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `INT_PROP_1` int NULL DEFAULT NULL,
  `INT_PROP_2` int NULL DEFAULT NULL,
  `LONG_PROP_1` bigint NULL DEFAULT NULL,
  `LONG_PROP_2` bigint NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `QRTZ_TRIGGERS` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of QRTZ_SIMPROP_TRIGGERS
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模块名称',
  `operation` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作人员',
  `ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'ip地址',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作地点',
  `state` tinyint(1) NOT NULL COMMENT '操作状态(0：成功，1：失败，2：异常)',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作方法',
  `req_parameter` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数',
  `req_mapping` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求方式',
  `exception` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '异常信息',
  `return_parameter` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回参数',
  `req_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求地址',
  `time` bigint NOT NULL COMMENT '消耗时间(ms)',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '接口描述',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3008 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (2867, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 103, '前台获取所有前台首页Banner图片', '2025-02-23 23:11:51', '2025-02-23 23:11:51', 0);
INSERT INTO `sys_log` VALUES (2868, '邮件发送', '邮件发送', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PublicController.askVerifyCode()', '[\"overhyyds@outlook.com\",\"register\"]', 'GET', NULL, '{\"code\":200,\"data\":\"验证码已发送，请注意查收！\",\"msg\":\"success\",\"timestamp\":0}', '/public/ask-code', 22506, '邮件发送', '2025-02-23 23:12:47', '2025-02-23 23:12:47', 0);
INSERT INTO `sys_log` VALUES (2869, '前台注册', '新增', 'unknown-1702606997', '127.0.0.1', '内网IP', 2, 'com.overthinker.cloud.web.controller.UserController.register()', '[{\"code\":\"923144\",\"email\":\"overhyyds@outlook.com\",\"password\":\"abcdefg\",\"username\":\"overthink\"}]', 'POST', '\r\n### Error updating database.  Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\r\n### The error may exist in com/overthinker/cloud/web/mapper/UserMapper.java (best guess)\r\n### The error may involve com.overthinker.cloud.web.mapper.UserMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO sys_user  ( nickname, username, password, gender, avatar, intro, email, register_type, register_ip,      login_time, create_time, update_time, is_deleted )  VALUES (  ?, ?, ?, ?, ?, ?, ?, ?, ?,      ?, ?, ?, ?  )\r\n### Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\n; Field \'register_address\' doesn\'t have a default value', NULL, '/user/register', 64, NULL, '2025-02-23 23:14:00', '2025-02-23 23:14:00', 0);
INSERT INTO `sys_log` VALUES (2870, '前台注册', '新增', 'unknown-1702606997', '127.0.0.1', '内网IP', 2, 'com.overthinker.cloud.web.controller.UserController.register()', '[{\"code\":\"923144\",\"email\":\"overhyyds@outlook.com\",\"password\":\"abcdefg\",\"username\":\"overthink\"}]', 'POST', '\r\n### Error updating database.  Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\r\n### The error may exist in com/overthinker/cloud/web/mapper/UserMapper.java (best guess)\r\n### The error may involve com.overthinker.cloud.web.mapper.UserMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO sys_user  ( nickname, username, password, gender, avatar, intro, email, register_type, register_ip,      login_time, create_time, update_time, is_deleted )  VALUES (  ?, ?, ?, ?, ?, ?, ?, ?, ?,      ?, ?, ?, ?  )\r\n### Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\n; Field \'register_address\' doesn\'t have a default value', NULL, '/user/register', 59, NULL, '2025-02-23 23:14:04', '2025-02-23 23:14:04', 0);
INSERT INTO `sys_log` VALUES (2871, '前台注册', '新增', 'unknown-1702606997', '127.0.0.1', '内网IP', 2, 'com.overthinker.cloud.web.controller.UserController.register()', '[{\"code\":\"923144\",\"email\":\"overhyyds@outlook.com\",\"password\":\"abcdefg\",\"username\":\"overthink\"}]', 'POST', '\r\n### Error updating database.  Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\r\n### The error may exist in com/overthinker/cloud/web/mapper/UserMapper.java (best guess)\r\n### The error may involve com.overthinker.cloud.web.mapper.UserMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO sys_user  ( nickname, username, password, gender, avatar, intro, email, register_type, register_ip,      login_time, create_time, update_time, is_deleted )  VALUES (  ?, ?, ?, ?, ?, ?, ?, ?, ?,      ?, ?, ?, ?  )\r\n### Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\n; Field \'register_address\' doesn\'t have a default value', NULL, '/user/register', 63, NULL, '2025-02-23 23:14:12', '2025-02-23 23:14:12', 0);
INSERT INTO `sys_log` VALUES (2872, '前台注册', '新增', 'unknown-1702606997', '127.0.0.1', '内网IP', 2, 'com.overthinker.cloud.web.controller.UserController.register()', '[{\"code\":\"923144\",\"email\":\"overhyyds@outlook.com\",\"password\":\"abcdefg\",\"username\":\"overthink\"}]', 'POST', '\r\n### Error updating database.  Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\r\n### The error may exist in com/overthinker/cloud/web/mapper/UserMapper.java (best guess)\r\n### The error may involve com.overthinker.cloud.web.mapper.UserMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO sys_user  ( nickname, username, password, gender, avatar, intro, email, register_type, register_ip,      login_time, create_time, update_time, is_deleted )  VALUES (  ?, ?, ?, ?, ?, ?, ?, ?, ?,      ?, ?, ?, ?  )\r\n### Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\n; Field \'register_address\' doesn\'t have a default value', NULL, '/user/register', 75455, NULL, '2025-02-23 23:18:14', '2025-02-23 23:18:14', 0);
INSERT INTO `sys_log` VALUES (2873, '菜单管理', '修改', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.MenuController.update()', '[{\"affix\":0,\"hideInMenu\":1,\"icon\":\"TabletTwoTone\",\"id\":70,\"isDisable\":0,\"keepAlive\":1,\"orderNum\":6,\"path\":\"http://blog.kuailemao.xyz\",\"routerType\":2,\"target\":\"_blank\",\"title\":\"跳转前台\",\"url\":\"\"}]', 'PUT', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/menu', 868, '修改菜单', '2025-02-24 01:10:39', '2025-02-24 01:10:39', 0);
INSERT INTO `sys_log` VALUES (2874, '菜单管理', '修改', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.MenuController.update()', '[{\"affix\":0,\"hideInMenu\":1,\"icon\":\"TabletTwoTone\",\"id\":70,\"isDisable\":0,\"keepAlive\":1,\"orderNum\":6,\"path\":\"http://baidu.com\",\"routerType\":2,\"target\":\"_blank\",\"title\":\"跳转前台\",\"url\":\"\"}]', 'PUT', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/menu', 839, '修改菜单', '2025-02-24 01:11:01', '2025-02-24 01:11:01', 0);
INSERT INTO `sys_log` VALUES (2875, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 3, '前台获取所有前台首页Banner图片', '2025-02-24 01:52:59', '2025-02-24 01:52:59', 0);
INSERT INTO `sys_log` VALUES (2876, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 01:53:16', '2025-02-24 01:53:16', 0);
INSERT INTO `sys_log` VALUES (2877, '文章管理', '上传图片', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.uploadArticleCover()', '[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@12f4df77]', 'POST', NULL, '{\"code\":200,\"data\":\"http://minioapi.overthinker.top/cloud/article/articleCover/530d1a23-a1b7-4117-98b2-a8a8c4f28293.png\",\"msg\":\"success\",\"timestamp\":0}', '/article/upload/articleCover', 644, '上传文章封面', '2025-02-24 02:02:49', '2025-02-24 02:02:49', 0);
INSERT INTO `sys_log` VALUES (2878, '文章管理', '新增', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.publish()', '[{\"articleContent\":\"\\n生活，宛如一幅绚丽多彩的画卷，每个人都是这画卷的创作者，用自己的经历、情感和梦想，描绘出独一无二的图案。它既有阳光明媚的日子，也会有风雨交加的时刻，正是这些不同的色彩和笔触，构成了生活的丰富与深邃。\\n\\n## 平凡日子里的小确幸\\n清晨，阳光透过窗帘的缝隙，温柔地洒在脸上，带来新一天的问候。走进厨房，煮上一杯香浓的咖啡，听着咖啡豆研磨的声音，仿佛是生活奏响的美妙序曲。伴随着咖啡的香气，翻开一本心仪已久的书籍，沉浸在文字的世界里，与作者进行一场跨越时空的对话。此时，时光仿佛慢了下来，每一个字都像是一颗璀璨的珍珠，串联起内心的宁静与满足。\\n\\n午后，漫步在公园的小径上，微风轻拂，树叶沙沙作响，似在诉说着大自然的故事。路边的花朵竞相绽放，红的像火，粉的像霞，白的像雪，它们在风中摇曳生姿，散发着迷人的芬芳。偶尔停下脚步，观察一只小蚂蚁忙碌的身影，看它如何搬运食物，如何与同伴交流协作，在这微小的生命中，感受到生命的顽强与坚韧。这样的午后，没有匆忙的脚步，没有繁重的工作，只有与自然的亲密接触，让人忘却一切烦恼，心中充满了对生活的热爱。\\n\\n夜晚，结束了一天的奔波，回到温馨的家中。与家人围坐在餐桌前，分享着一天的见闻和喜怒哀乐。餐桌上摆满了热气腾腾的饭菜，每一道菜都蕴含着家人的关爱与用心。灯光柔和地洒在每个人的脸上，欢声笑语回荡在房间里，这一刻，亲情的温暖如同一股暖流，流淌在心中，让人感受到生活的美好与幸福。\\n\\n## 面对挑战时的勇气与成长\\n生活并非总是一帆风顺，它也会给我们带来各种各样的挑战。工作上的压力、人际关系的困扰、梦想与现实的差距，都可能让我们感到疲惫和迷茫。然而，正是这些挑战，如同磨刀石一般，磨砺着我们的意志，让我们不断成长和进步。\\n\\n曾经，我在工作中面临着一个巨大的项目，时间紧迫，任务艰巨，压力让我几乎喘不过气来。无数次，我在深夜里对着电脑屏幕，思考着项目的方案和细节，焦虑和不安如影随形。但是，我没有选择逃避，而是鼓起勇气，一步一个脚印地去解决问题。我主动向同事请教，查阅大量的资料，不断尝试新的方法和思路。在这个过程中，我遇到了许多困难和挫折，但每一次克服困难，都让我感受到自己的成长和进步。最终，项目顺利完成，那一刻，我心中充满了成就感，也明白了只要有勇气面对挑战，就没有什么能够阻挡我们前进的步伐。\\n\\n在人际关系中，我们也难免会遇到矛盾和冲突。有时候，我们会因为误解而与朋友产生隔阂，或者因为观点不同而与家人发生争执。这些经历可能会让我们感到伤心和难过，但同时也是我们学习如何理解他人、如何沟通和解决问题的宝贵机会。通过真诚地沟通和换位思考，我们往往能够化解矛盾，修复关系，让彼此的感情更加深厚。在这个过程中，我们学会了包容、学会了关爱，也更加懂得珍惜身边的人。\\n\\n## 追寻梦想，点亮生活的光芒\\n生活中，梦想是一束照亮前行道路的光，它让我们的生活充满了希望和动力。每个人都有自己的梦想，或许是成为一名优秀的艺术家，用画笔描绘出心中的美好世界；或许是成为一名救死扶伤的医生，帮助患者战胜疾病，重获健康；或许是成为一名旅行家，踏遍世界各地，领略不同的风土人情。无论梦想是什么，它都承载着我们对生活的热爱和向往。\\n\\n为了实现梦想，我们需要付出努力和汗水。这意味着要在无数个日日夜夜中坚持学习，不断提升自己的能力；要在面对困难和挫折时，不轻易放弃，始终保持坚定的信念。在追寻梦想的道路上，我们会遇到各种各样的诱惑和干扰，但只要心中有梦想，我们就能够抵御这些诱惑，坚定地朝着目标前进。当我们通过自己的努力，离梦想越来越近时，那种喜悦和满足是无法用言语来形容的。梦想不仅让我们实现了自我价值，也让我们的生活变得更加充实和有意义。\\n\\n生活，就是这样一个充满惊喜与挑战的旅程。它既有平凡日子里的小确幸，让我们感受到生活的温暖与美好；也有面对挑战时的艰辛与成长，让我们变得更加坚强和成熟；更有追寻梦想的激情与动力，让我们的生活绽放出耀眼的光芒。在这趟旅程中，我们要用心去感受每一个瞬间，用爱去拥抱生活的每一面，因为正是这些丰富多彩的经历，构成了我们独一无二的人生。让我们怀揣着对生活的热爱，勇敢地踏上这趟奇妙的旅程，去书写属于自己的精彩篇章。 \",\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/530d1a23-a1b7-4117-98b2-a8a8c4f28293.png\",\"articleTitle\":\"一场充满惊喜与挑战的旅程\",\"articleType\":1,\"categoryId\":13,\"isTop\":1,\"status\":1,\"tagId\":[14]}]', 'POST', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/article/publish', 192, '发布文章', '2025-02-24 02:02:50', '2025-02-24 02:02:50', 0);
INSERT INTO `sys_log` VALUES (2879, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 02:03:39', '2025-02-24 02:03:39', 0);
INSERT INTO `sys_log` VALUES (2880, '新增分类', '新增', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.CategoryController.addCategory()', '[{\"categoryName\":\"科技\"}]', 'PUT', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/category', 186, '新增分类-文章列表', '2025-02-24 02:05:49', '2025-02-24 02:05:49', 0);
INSERT INTO `sys_log` VALUES (2881, '标签管理', '新增', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.TagController.addTag()', '[{\"id\":15,\"tagName\":\"人工智能\"}]', 'PUT', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/tag', 198, '新增标签-文章列表', '2025-02-24 02:06:04', '2025-02-24 02:06:04', 0);
INSERT INTO `sys_log` VALUES (2882, '文章管理', '上传图片', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.uploadArticleCover()', '[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@61378fc]', 'POST', NULL, '{\"code\":200,\"data\":\"http://minioapi.overthinker.top/cloud/article/articleCover/1ea84532-cdb1-4b84-8c05-a523d184aee6.png\",\"msg\":\"success\",\"timestamp\":0}', '/article/upload/articleCover', 158, '上传文章封面', '2025-02-24 02:06:17', '2025-02-24 02:06:17', 0);
INSERT INTO `sys_log` VALUES (2883, '文章管理', '新增', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.publish()', '[{\"articleContent\":\"当然，我可以为你生成一篇随机的文章。以下是一篇关于“未来科技发展趋势”的示例文章：\\n\\n---\\n\\n**未来科技发展趋势：探索未知，塑造未来**\\n\\n在21世纪的今天，科技正以前所未有的速度改变着我们的生活。从智能手机到无人驾驶汽车，从人工智能到量子计算，科技的每一次进步都在重塑我们的世界。那么，未来的科技发展又将走向何方？本文将探讨几个关键领域，展望未来的科技发展趋势。\\n\\n**一、人工智能：从辅助到主导**\\n\\n人工智能（AI）无疑是当前科技领域的热门话题。随着深度学习、自然语言处理等技术的不断进步，AI正在逐渐从辅助工具转变为主导力量。在未来，我们可以预见AI将在更多领域发挥重要作用。例如，在医疗领域，AI将能够辅助医生进行更精准的诊断和治疗；在教育领域，AI将为学生提供个性化的学习方案，提高学习效率。此外，AI还将推动自动驾驶技术的进一步发展，使交通更加安全、高效。\\n\\n**二、量子计算：突破传统限制**\\n\\n量子计算是近年来备受瞩目的新兴技术。与传统计算机相比，量子计算机具有更高的计算速度和更强的处理能力。随着量子计算技术的不断成熟，它将在材料科学、药物研发、金融分析等领域发挥巨大作用。例如，在药物研发中，量子计算可以模拟复杂的分子结构，加速新药的发现；在金融分析中，量子计算可以处理大量数据，提高投资决策的准确性。\\n\\n**三、区块链：重塑信任机制**\\n\\n区块链技术以其去中心化、不可篡改的特点，正在逐渐改变我们对信任的理解。在未来，区块链将在金融、物流、版权保护等领域发挥重要作用。例如，在金融领域，区块链可以降低交易成本，提高交易效率；在物流领域，区块链可以确保货物的真实性和安全性；在版权保护方面，区块链可以确保创作者的知识产权得到充分保护。\\n\\n**四、生物技术：探索生命奥秘**\\n\\n生物技术是近年来发展迅速的领域之一。随着基因编辑、合成生物学等技术的不断进步，我们有望在未来实现更多关于生命的突破。例如，在医疗领域，基因编辑技术可以用于治疗遗传性疾病；在农业领域，合成生物学可以培育出更耐旱、抗病虫害的作物。此外，生物技术还将推动人类寿命的延长和生命质量的提升。\\n\\n**五、可持续发展技术：应对全球挑战**\\n\\n面对全球气候变化和资源短缺的挑战，可持续发展技术成为未来科技发展的重要方向。例如，在能源领域，可再生能源技术将逐渐取代化石燃料，成为主要的能源来源；在环保领域，废物回收和再利用技术将减少资源浪费和环境污染。此外，智能城市、绿色建筑等概念也将推动城市的可持续发展。\\n\\n**结语**\\n\\n未来的科技发展趋势充满挑战与机遇。随着人工智能、量子计算、区块链、生物技术和可持续发展技术等领域的不断进步，我们有望看到一个更加智能、高效、可持续的世界。然而，科技的发展也伴随着伦理、隐私和安全等问题。因此，在追求科技进步的同时，我们也需要关注这些问题，确保科技的发展能够真正造福人类。\\n\\n---\\n\",\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/1ea84532-cdb1-4b84-8c05-a523d184aee6.png\",\"articleTitle\":\"探索未知，塑造未来\",\"articleType\":1,\"categoryId\":14,\"isTop\":1,\"status\":1,\"tagId\":[15]}]', 'POST', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/article/publish', 174, '发布文章', '2025-02-24 02:06:18', '2025-02-24 02:06:18', 0);
INSERT INTO `sys_log` VALUES (2884, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 02:06:23', '2025-02-24 02:06:23', 0);
INSERT INTO `sys_log` VALUES (2885, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 02:07:04', '2025-02-24 02:07:04', 0);
INSERT INTO `sys_log` VALUES (2886, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 02:07:27', '2025-02-24 02:07:27', 0);
INSERT INTO `sys_log` VALUES (2887, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 19, '前台相册或照片列表', '2025-02-24 02:07:57', '2025-02-24 02:07:57', 0);
INSERT INTO `sys_log` VALUES (2888, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 8, '前台相册或照片列表', '2025-02-24 02:07:59', '2025-02-24 02:07:59', 0);
INSERT INTO `sys_log` VALUES (2889, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 12, '前台相册或照片列表', '2025-02-24 02:08:01', '2025-02-24 02:08:01', 0);
INSERT INTO `sys_log` VALUES (2890, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 10, '前台相册或照片列表', '2025-02-24 02:08:02', '2025-02-24 02:08:02', 0);
INSERT INTO `sys_log` VALUES (2891, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 10, '前台相册或照片列表', '2025-02-24 02:08:03', '2025-02-24 02:08:03', 0);
INSERT INTO `sys_log` VALUES (2892, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 12, '前台相册或照片列表', '2025-02-24 02:08:04', '2025-02-24 02:08:04', 0);
INSERT INTO `sys_log` VALUES (2893, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 02:08:06', '2025-02-24 02:08:06', 0);
INSERT INTO `sys_log` VALUES (2894, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 02:13:56', '2025-02-24 02:13:56', 0);
INSERT INTO `sys_log` VALUES (2895, '友链管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.LinkController.backList()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"background\":\"http://cdn.kuailemao.lielfw.cn/articleCover/21676717033297579.jpg\",\"createTime\":1705931708000,\"description\":\"无语小站无语小站\",\"email\":\"3490223402@qq.com\",\"id\":18,\"isCheck\":1,\"name\":\"无语小站\",\"url\":\"  http://localhost:99/\",\"userName\":\"ADMIN\"}],\"msg\":\"success\",\"timestamp\":0}', '/link/back/list', 7, '后台友链列表', '2025-02-24 02:14:04', '2025-02-24 02:14:04', 0);
INSERT INTO `sys_log` VALUES (2896, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 02:14:14', '2025-02-24 02:14:14', 0);
INSERT INTO `sys_log` VALUES (2897, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 02:16:23', '2025-02-24 02:16:23', 0);
INSERT INTO `sys_log` VALUES (2898, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 02:16:23', '2025-02-24 02:16:23', 0);
INSERT INTO `sys_log` VALUES (2899, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 02:21:29', '2025-02-24 02:21:29', 0);
INSERT INTO `sys_log` VALUES (2900, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 9, '前台相册或照片列表', '2025-02-24 02:21:40', '2025-02-24 02:21:40', 0);
INSERT INTO `sys_log` VALUES (2901, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 02:23:24', '2025-02-24 02:23:24', 0);
INSERT INTO `sys_log` VALUES (2902, '文章管理', '上传图片', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.uploadArticleImage()', '[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@7ef69b6a]', 'POST', NULL, '{\"code\":200,\"data\":\"http://minioapi.overthinker.top/cloud/article/articleImage/72116a1f-9237-469e-bc20-81745bfdb75a.png\",\"msg\":\"success\",\"timestamp\":0}', '/article/upload/articleImage', 311, '上传文章图片', '2025-02-24 02:24:51', '2025-02-24 02:24:51', 0);
INSERT INTO `sys_log` VALUES (2903, '标签管理', '新增', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.TagController.addTag()', '[{\"id\":16,\"tagName\":\"社会\"}]', 'PUT', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/tag', 529, '新增标签-文章列表', '2025-02-24 02:25:30', '2025-02-24 02:25:30', 0);
INSERT INTO `sys_log` VALUES (2904, '文章管理', '上传图片', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.uploadArticleCover()', '[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@5bb9310d]', 'POST', NULL, '{\"code\":200,\"data\":\"http://minioapi.overthinker.top/cloud/article/articleCover/b8b2721c-2d45-4bc5-8d2e-81c1bd5ffca8.png\",\"msg\":\"success\",\"timestamp\":0}', '/article/upload/articleCover', 637, '上传文章封面', '2025-02-24 02:25:46', '2025-02-24 02:25:46', 0);
INSERT INTO `sys_log` VALUES (2905, '文章管理', '新增', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.publish()', '[{\"articleContent\":\"![](http://minioapi.overthinker.top/cloud/article/articleImage/72116a1f-9237-469e-bc20-81745bfdb75a.png)\\n\\n\\n**构建生态宜居城市的绿色基石**\\n\\n在快速城市化的今天，高楼大厦如雨后春笋般涌现，为城市带来了前所未有的繁荣。然而，这种繁荣背后也隐藏着诸多问题，如空气污染、热岛效应、生物多样性的减少等。面对这些挑战，城市绿化作为构建生态宜居城市的关键一环，其重要性日益凸显。本文将探讨城市绿化的多重价值、面临的挑战以及实现策略。\\n\\n**一、城市绿化的多重价值**\\n\\n1. **改善空气质量**：植物通过光合作用吸收二氧化碳，释放氧气，有效缓解城市空气污染问题。同时，绿植还能吸附空气中的尘埃和有害物质，提升空气质量。\\n\\n2. **调节城市微气候**：城市绿地如同城市的“天然空调”，通过蒸腾作用降低周围温度，缓解热岛效应。此外，绿地还能增加空气湿度，改善城市小气候。\\n\\n3. **提升居民生活质量**：绿色空间为城市居民提供了休闲娱乐的好去处，有助于缓解生活压力，提升心理健康。同时，绿地的存在也增强了城市的审美价值，提升了居民的生活满意度。\\n\\n4. **保护生物多样性**：城市绿地是城市生物多样性的重要栖息地，为鸟类、昆虫等野生动物提供了生存空间，有助于维护生态平衡。\\n\\n**二、城市绿化面临的挑战**\\n\\n尽管城市绿化的价值显而易见，但在实施过程中仍面临诸多挑战。首先，城市用地紧张，绿地规划往往受到空间限制。其次，绿化维护成本较高，需要持续的资金投入。此外，公众对绿化的认识和参与度也是影响绿化效果的关键因素。\\n\\n**三、城市绿化的实现策略**\\n\\n1. **科学规划，合理布局**：在城市规划中，应将绿地作为城市基础设施的重要组成部分，科学规划，合理布局。通过建设城市公园、街头绿地、屋顶花园等多种形式，增加城市绿地面积。\\n\\n2. **鼓励公众参与，提升绿化意识**：通过宣传教育、社区活动等方式，提高公众对城市绿化的认识，鼓励居民参与绿化种植、养护等活动，形成全民共建共享的良好氛围。\\n\\n3. **引入市场机制，创新绿化模式**：探索政府引导、市场运作的绿化模式，吸引社会资本参与城市绿化建设。如通过PPP（政府和社会资本合作）模式，引入专业绿化企业进行绿化项目的投资、建设和运营。\\n\\n4. **加强科技支撑，提升绿化效率**：运用现代科技手段，如智能灌溉、远程监控等，提高绿化养护的精准度和效率。同时，研发适应城市环境的优良绿化树种，丰富城市绿化品种。\\n\\n5. **注重生态修复，保护自然遗产**：在城市绿化过程中，应注重对受损生态系统的修复，如利用城市废弃地建设生态公园，既美化城市环境，又保护自然遗产。\\n\\n**结语**\\n\\n城市绿化是构建生态宜居城市的重要基石，对于提升城市品质、改善居民生活质量具有不可替代的作用。面对城市化进程中的诸多挑战，我们应积极探索科学的绿化策略，鼓励公众参与，加强科技支撑，共同打造绿色、健康、和谐的城市生态环境。\\n\\n---\\n\\n请注意，以上文章为随机生成，仅供参考，具体数据和情况可能因地区和时间的不同而有所变化。\",\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/b8b2721c-2d45-4bc5-8d2e-81c1bd5ffca8.png\",\"articleTitle\":\"城市绿化\",\"articleType\":1,\"categoryId\":13,\"isTop\":1,\"status\":1,\"tagId\":[16]}]', 'POST', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/article/publish', 245, '发布文章', '2025-02-24 02:25:46', '2025-02-24 02:25:46', 0);
INSERT INTO `sys_log` VALUES (2906, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 02:25:54', '2025-02-24 02:25:54', 0);
INSERT INTO `sys_log` VALUES (2907, '文章管理', '上传图片', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.uploadArticleCover()', '[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@57416e40]', 'POST', NULL, '{\"code\":200,\"data\":\"http://minioapi.overthinker.top/cloud/article/articleCover/3655f106-2452-4403-91bc-ac65b3cfe8c7.png\",\"msg\":\"success\",\"timestamp\":0}', '/article/upload/articleCover', 597, '上传文章封面', '2025-02-24 02:28:18', '2025-02-24 02:28:18', 0);
INSERT INTO `sys_log` VALUES (2908, '文章管理', '新增', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.publish()', '[{\"articleContent\":\"\\n\\n**：迎接变革，塑造未来**\\n\\n在21世纪的今天，我们正经历着前所未有的职业与工作方式的变革。随着科技的飞速发展、全球化的深入以及社会需求的不断变化，未来的工作与职业发展将呈现出一系列新的趋势。本文将探讨这些趋势，并思考它们如何影响我们的职业道路和工作方式。\\n\\n**一、远程工作与灵活工作制的兴起**\\n\\n随着互联网的普及和技术的进步，远程工作已成为越来越多人的选择。特别是在疫情期间，远程工作更是成为了许多行业的标配。未来，随着云计算、大数据、人工智能等技术的进一步发展，远程工作和灵活工作制将更加普及。这种工作方式不仅提高了工作效率，还为员工提供了更多的自由和灵活性，有助于提升工作满意度和生活质量。\\n\\n**二、技能导向的职业发展**\\n\\n在未来的职场中，技能将成为衡量个人价值的重要标准。随着技术的不断进步和行业的快速变化，许多传统职业正在被自动化取代，而新兴职业则不断涌现。因此，持续学习和掌握新技能将成为职场人士必备的能力。未来，职业发展将更加注重个人的技能提升和跨界能力，而不仅仅是学历和工作经验。\\n\\n**三、项目制与团队合作的加强**\\n\\n随着企业组织结构的扁平化和项目管理的普及，项目制和团队合作将成为未来职场的主流模式。在这种模式下，员工将更多地以项目为单位进行工作，与来自不同背景和专业的团队成员紧密合作。这种工作方式不仅提高了工作效率和创新能力，还促进了员工之间的交流和协作，有助于构建更加和谐、包容的职场文化。\\n\\n**四、创业与自主就业的兴起**\\n\\n随着创业环境的改善和自主就业政策的支持，越来越多的人选择创业或自主就业作为自己的职业道路。未来，随着数字化、智能化等技术的进一步发展，创业和自主就业的机会将更加丰富多样。这种职业选择不仅有助于个人实现自我价值，还能为社会创造更多的就业机会和经济增长点。\\n\\n**五、关注员工福利与心理健康**\\n\\n在未来的职场中，员工福利和心理健康将受到更多的关注。随着工作压力的增大和生活节奏的加快，员工的身心健康问题日益凸显。因此，企业将更加重视员工福利和心理健康的保障，通过提供健康保险、心理咨询等服务，帮助员工缓解压力、提升幸福感。\\n\\n**结语**\\n\\n未来工作与职业发展的新趋势将深刻影响我们的职业道路和工作方式。面对这些变革，我们应保持开放的心态和积极的态度，不断学习和提升自己的技能和能力。同时，我们也应关注自己的身心健康和职业发展需求，努力在变革中找到属于自己的职业道路和工作方式。只有这样，我们才能在未来的职场中立于不败之地，迎接更加美好的未来。\\n\\n---\\n\\n\",\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/3655f106-2452-4403-91bc-ac65b3cfe8c7.png\",\"articleTitle\":\"未来工作与职业发展的新趋势\",\"articleType\":1,\"categoryId\":14,\"isTop\":0,\"status\":1,\"tagId\":[16]}]', 'POST', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/article/publish', 162, '发布文章', '2025-02-24 02:28:18', '2025-02-24 02:28:18', 0);
INSERT INTO `sys_log` VALUES (2909, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 02:28:36', '2025-02-24 02:28:36', 0);
INSERT INTO `sys_log` VALUES (2910, '文章管理', '上传图片', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.uploadArticleCover()', '[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@1423b46a]', 'POST', NULL, '{\"code\":200,\"data\":\"http://minioapi.overthinker.top/cloud/article/articleCover/e2d1ec3d-5e18-4e42-9fa4-cce9c4d2a1f5.png\",\"msg\":\"success\",\"timestamp\":0}', '/article/upload/articleCover', 1351, '上传文章封面', '2025-02-24 02:29:37', '2025-02-24 02:29:37', 0);
INSERT INTO `sys_log` VALUES (2911, '文章管理', '新增', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.publish()', '[{\"articleContent\":\"\\n\\n**科技与人文的融合**\\n\\n在人类历史的长河中，教育始终是推动社会进步和文明发展的重要力量。随着科技的飞速发展和社会需求的不断变化，未来教育正面临着前所未有的挑战与机遇。本文旨在探讨未来教育的创新之路，特别是科技与人文如何在这一进程中实现深度融合，共同塑造更加公平、高效、个性化的教育体系。\\n\\n**一、科技驱动的教育变革**\\n\\n近年来，人工智能、大数据、云计算等先进技术的快速发展，为教育领域带来了深刻的变革。智能教学系统能够根据学生的学习进度和兴趣，提供个性化的学习路径和资源，从而提高学习效率。同时，虚拟现实（VR）、增强现实（AR）等技术的应用，更是为学生创造了沉浸式的学习环境，使抽象知识变得直观易懂。此外，在线学习平台的兴起，打破了地域限制，让优质教育资源得以广泛传播，促进了教育公平。\\n\\n**二、人文精神的回归与融合**\\n\\n然而，科技的快速发展也带来了教育领域的某些“异化”现象。过度依赖技术可能导致学生缺乏批判性思维、创新能力和人际交往能力。因此，未来教育在追求技术革新的同时，必须回归人文精神，强调学生的全面发展。这包括培养学生的价值观、社会责任感、审美情趣以及跨文化交流能力。通过人文学科的融入，使教育不仅仅是知识的传授，更是品格的塑造和精神的滋养。\\n\\n**三、创新教育模式：科技与人文的融合实践**\\n\\n1. **项目式学习（PBL）**：结合科技手段，如在线协作平台、数据分析工具等，开展跨学科的项目式学习。学生围绕真实世界的问题进行探究，不仅锻炼了问题解决能力，还促进了团队合作和批判性思维的发展。\\n\\n2. **翻转课堂**：利用视频、在线课程等数字化资源，让学生在课外自主学习基础知识，课堂时间则用于深度讨论、实践操作和个性化指导。这种模式既发挥了科技的优势，又强调了师生互动和人文关怀。\\n\\n3. **社区参与与社会实践**：鼓励学生参与社区服务、环保项目等社会实践活动，通过亲身体验，增进对社会的理解，培养公民意识和社会责任感。同时，利用科技手段记录、分析和反思这些经历，深化学习效果。\\n\\n**四、面向未来的教育挑战与应对策略**\\n\\n尽管科技与人文的融合为教育创新提供了无限可能，但仍面临诸多挑战。如教育资源的分配不均、教师角色的转变与培训、学生自主学习能力的培养等。为此，政府、学校、企业和社会各界需共同努力，制定更加公平、可持续的教育政策，加强教师队伍建设，推动教育技术创新与应用，同时注重学生的人文素养培养。\\n\\n**结语**\\n\\n未来教育的创新之路，是一条科技与人文深度融合的探索之旅。在这条路上，我们既要充分利用科技的力量，提升教育的效率和质量，也要坚守人文的底线，关注学生的全面发展。只有这样，我们才能培养出既有扎实知识，又具备创新精神和社会责任感的未来公民，共同迎接更加美好的明天。\\n\\n---\\n\",\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/e2d1ec3d-5e18-4e42-9fa4-cce9c4d2a1f5.png\",\"articleTitle\":\"探索未来教育的创新之路\",\"articleType\":1,\"categoryId\":13,\"isTop\":0,\"status\":1,\"tagId\":[16]}]', 'POST', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/article/publish', 134, '发布文章', '2025-02-24 02:29:38', '2025-02-24 02:29:38', 0);
INSERT INTO `sys_log` VALUES (2912, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 9, '前台获取所有前台首页Banner图片', '2025-02-24 02:36:50', '2025-02-24 02:36:50', 0);
INSERT INTO `sys_log` VALUES (2913, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 3, '前台获取所有前台首页Banner图片', '2025-02-24 02:37:40', '2025-02-24 02:37:40', 0);
INSERT INTO `sys_log` VALUES (2914, '文章管理', '上传图片', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.uploadArticleCover()', '[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@1a4b04f]', 'POST', NULL, '{\"code\":200,\"data\":\"http://minioapi.overthinker.top/cloud/article/articleCover/8b4eb727-09b8-457d-a710-a1f672a29316.png\",\"msg\":\"success\",\"timestamp\":0}', '/article/upload/articleCover', 1093, '上传文章封面', '2025-02-24 02:44:16', '2025-02-24 02:44:16', 0);
INSERT INTO `sys_log` VALUES (2915, '文章管理', '新增', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.publish()', '[{\"articleContent\":\"\\n\\n**智能与绿色的交响曲**\\n\\n在21世纪的曙光中，我们正见证着城市面貌的深刻变革。随着科技的飞速进步和全球环保意识的增强，未来城市正逐步向我们展现出一幅智能与绿色交织的宏伟蓝图。这是一场前所未有的城市革命，它不仅仅是建筑和技术的革新，更是人类生活方式和思维方式的深刻转变。\\n\\n**一、智能城市的崛起**\\n\\n智能城市，作为未来城市的核心形态，正在全球范围内迅速崛起。它通过物联网、大数据、云计算、人工智能等先进技术，将城市的基础设施、公共服务、交通出行、环境保护等各个方面进行智能化改造和升级。智能城市不仅提高了城市的运行效率和居民的生活质量，还带来了前所未有的便捷性和安全性。\\n\\n在智能城市中，交通出行将变得更加智能和高效。自动驾驶汽车、智能交通管理系统、智能停车系统等技术的应用，将大大缓解城市交通拥堵问题，提高出行效率。同时，智能城市还能通过大数据分析，预测和应对各种城市问题，如公共安全、环境污染等，从而确保城市的稳定和安全。\\n\\n**二、绿色城市的呼唤**\\n\\n与智能城市并行发展的，是绿色城市的理念和实践。在全球气候变化和环境污染日益严重的背景下，绿色城市成为了未来城市发展的必然趋势。绿色城市强调可持续发展，注重节能减排、资源循环利用和生态环境保护。\\n\\n在绿色城市中，绿色建筑将成为主流。这些建筑通过采用先进的节能技术和材料，大幅降低能耗和碳排放。同时，绿色城市还将大力发展可再生能源，如太阳能、风能等，以减少对化石燃料的依赖。此外，绿色城市还注重城市绿化和生态保护，通过建设城市公园、湿地保护区等，为城市居民提供优美的生态环境和休闲空间。\\n\\n**三、智能与绿色的交响曲**\\n\\n未来城市的发展，将是智能与绿色交织的交响曲。智能城市的技术优势，将为绿色城市的发展提供有力支持。例如，智能城市中的大数据分析技术，可以帮助城市管理者更好地了解城市能源使用情况，从而制定更加科学的节能减排政策。同时，智能城市中的物联网技术，可以实时监测和控制城市中的各种设备，实现能源的高效利用和资源的循环利用。\\n\\n而绿色城市的发展理念，也将为智能城市的建设提供重要指导。绿色城市强调可持续发展和生态平衡，这将促使智能城市在发展过程中，更加注重环境保护和生态平衡。同时，绿色城市中的绿色建筑和可再生能源技术，也将为智能城市提供更加环保和可持续的能源解决方案。\\n\\n**四、未来城市的展望**\\n\\n未来城市的发展，将是一场智能与绿色的交响曲。在这场交响曲中，我们将看到科技与自然、人类与环境的和谐共生。未来城市将成为一个充满智慧、活力和生机的美好家园，为人类社会带来前所未有的繁荣和进步。\\n\\n然而，未来城市的发展也面临着诸多挑战。如何平衡智能与绿色的发展关系？如何确保城市发展的可持续性和生态平衡？这些问题需要我们不断探索和实践，以找到最佳的解决方案。\\n\\n总之，未来城市的发展将是一场充满挑战和机遇的旅程。我们相信，在科技与自然、人类与环境的共同努力下，未来城市一定能够成为一个更加美好、更加繁荣的家园。\\n\\n---\\n\",\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/8b4eb727-09b8-457d-a710-a1f672a29316.png\",\"articleTitle\":\"未来城市\",\"articleType\":1,\"categoryId\":14,\"isTop\":0,\"status\":1,\"tagId\":[16]}]', 'POST', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/article/publish', 223, '发布文章', '2025-02-24 02:44:16', '2025-02-24 02:44:16', 0);
INSERT INTO `sys_log` VALUES (2916, '文章管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.listArticle()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/8b4eb727-09b8-457d-a710-a1f672a29316.png\",\"articleTitle\":\"未来城市\",\"articleType\":1,\"categoryId\":14,\"categoryName\":\"科技\",\"createTime\":1740336256000,\"id\":47,\"isTop\":0,\"status\":1,\"tagsName\":[\"社会\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/e2d1ec3d-5e18-4e42-9fa4-cce9c4d2a1f5.png\",\"articleTitle\":\"探索未来教育的创新之路\",\"articleType\":1,\"categoryId\":13,\"categoryName\":\"生活\",\"createTime\":1740335378000,\"id\":46,\"isTop\":0,\"status\":1,\"tagsName\":[\"社会\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/3655f106-2452-4403-91bc-ac65b3cfe8c7.png\",\"articleTitle\":\"未来工作与职业发展的新趋势\",\"articleType\":1,\"categoryId\":14,\"categoryName\":\"科技\",\"createTime\":1740335298000,\"id\":45,\"isTop\":0,\"status\":1,\"tagsName\":[\"社会\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/b8b2721c-2d45-4bc5-8d2e-81c1bd5ffca8.png\",\"articleTitle\":\"城市绿化\",\"articleType\":1,\"categoryId\":13,\"categoryName\":\"生活\",\"createTime\":1740335146000,\"id\":44,\"isTop\":1,\"status\":1,\"tagsName\":[\"社会\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/1ea84532-cdb1-4b84-8c05-a523d184aee6.png\",\"articleTitle\":\"探索未知，塑造未来\",\"articleType\":1,\"categoryId\":14,\"categoryName\":\"科技\",\"createTime\":1740333977000,\"id\":43,\"isTop\":1,\"status\":1,\"tagsName\":[\"人工智能\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/530d1a23-a1b7-4117-98b2-a8a8c4f28293.png\",\"articleTitle\":\"一场充满惊喜与挑战的旅程\",\"articleType\":1,\"categoryId\":13,\"categoryName\":\"生活\",\"createTime\":1740333770000,\"id\":42,\"isTop\":1,\"status\":1,\"tagsName\":[\"测试标签\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/41455699-438b-4e36-aa3f-eec094383386.png\",\"articleTitle\":\"你好\",\"articleType\":1,\"categoryId\":13,\"categoryName\":\"生活\",\"createTime\":1740307295000,\"id\":41,\"isTop\":0,\"status\":1,\"tagsName\":[\"测试标签\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0}],\"msg\":\"success\",\"timestamp\":0}', '/article/back/list', 42, '获取所有的文章列表', '2025-02-24 02:44:20', '2025-02-24 02:44:20', 0);
INSERT INTO `sys_log` VALUES (2917, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 02:47:54', '2025-02-24 02:47:54', 0);
INSERT INTO `sys_log` VALUES (2918, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 87, '前台相册或照片列表', '2025-02-24 02:47:55', '2025-02-24 02:47:55', 0);
INSERT INTO `sys_log` VALUES (2919, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 10, '前台相册或照片列表', '2025-02-24 02:47:58', '2025-02-24 02:47:58', 0);
INSERT INTO `sys_log` VALUES (2920, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 10, '前台相册或照片列表', '2025-02-24 02:48:17', '2025-02-24 02:48:17', 0);
INSERT INTO `sys_log` VALUES (2921, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 02:48:21', '2025-02-24 02:48:21', 0);
INSERT INTO `sys_log` VALUES (2922, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1738490156000,\"id\":39,\"path\":\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"size\":2855908,\"sortOrder\":1,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 3, '后台获取所有前台首页Banner图片', '2025-02-24 02:48:40', '2025-02-24 02:48:40', 0);
INSERT INTO `sys_log` VALUES (2923, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1738490156000,\"id\":39,\"path\":\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"size\":2855908,\"sortOrder\":1,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 1, '后台获取所有前台首页Banner图片', '2025-02-24 02:48:57', '2025-02-24 02:48:57', 0);
INSERT INTO `sys_log` VALUES (2924, '信息管理', '上传图片', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.WebsiteInfoController.uploadBackground()', '[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@9890cf4]', 'POST', NULL, '{\"code\":200,\"data\":\"http://minioapi.overthinker.top/cloud/websiteInfo/background/4b55d71d-45ce-4dd4-a51f-30e8cd23fadc.png\",\"msg\":\"success\",\"timestamp\":0}', '/websiteInfo/upload/background', 1754, '上传站长资料卡背景', '2025-02-24 02:49:06', '2025-02-24 02:49:06', 0);
INSERT INTO `sys_log` VALUES (2925, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 4, '前台获取所有前台首页Banner图片', '2025-02-24 02:49:45', '2025-02-24 02:49:45', 0);
INSERT INTO `sys_log` VALUES (2926, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 02:49:58', '2025-02-24 02:49:58', 0);
INSERT INTO `sys_log` VALUES (2927, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 02:50:04', '2025-02-24 02:50:04', 0);
INSERT INTO `sys_log` VALUES (2928, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1738490156000,\"id\":39,\"path\":\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"size\":2855908,\"sortOrder\":1,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 1, '后台获取所有前台首页Banner图片', '2025-02-24 02:59:00', '2025-02-24 02:59:00', 0);
INSERT INTO `sys_log` VALUES (2929, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1738490156000,\"id\":39,\"path\":\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"size\":2855908,\"sortOrder\":1,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 2, '后台获取所有前台首页Banner图片', '2025-02-24 02:59:25', '2025-02-24 02:59:25', 0);
INSERT INTO `sys_log` VALUES (2930, '信息管理', '新增', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.uploadArticleImage()', '[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@52333762]', 'POST', NULL, '{\"code\":200,\"data\":{\"createTime\":1740337172055,\"id\":40,\"path\":\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"size\":3713281,\"sortOrder\":2,\"type\":\"image/png\",\"userId\":1},\"msg\":\"success\",\"timestamp\":0}', '/banners/upload/banner', 1398, '添加前台首页Banner图片', '2025-02-24 02:59:32', '2025-02-24 02:59:32', 0);
INSERT INTO `sys_log` VALUES (2931, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1738490156000,\"id\":39,\"path\":\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"size\":2855908,\"sortOrder\":1,\"type\":\"image/png\",\"userId\":1},{\"createTime\":1740337172000,\"id\":40,\"path\":\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"size\":3713281,\"sortOrder\":2,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 2, '后台获取所有前台首页Banner图片', '2025-02-24 02:59:32', '2025-02-24 02:59:32', 0);
INSERT INTO `sys_log` VALUES (2932, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 03:02:01', '2025-02-24 03:02:01', 0);
INSERT INTO `sys_log` VALUES (2933, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 03:02:08', '2025-02-24 03:02:08', 0);
INSERT INTO `sys_log` VALUES (2934, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 3, '前台获取所有前台首页Banner图片', '2025-02-24 03:02:12', '2025-02-24 03:02:12', 0);
INSERT INTO `sys_log` VALUES (2935, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 03:02:15', '2025-02-24 03:02:15', 0);
INSERT INTO `sys_log` VALUES (2936, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 03:09:48', '2025-02-24 03:09:48', 0);
INSERT INTO `sys_log` VALUES (2937, '相册管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.backList()', '[1,10,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/back/list', 16, '后台相册或照片列表', '2025-02-24 03:10:30', '2025-02-24 03:10:30', 0);
INSERT INTO `sys_log` VALUES (2938, '相册管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.backList()', '[1,10,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/back/list', 9, '后台相册或照片列表', '2025-02-24 03:10:30', '2025-02-24 03:10:30', 0);
INSERT INTO `sys_log` VALUES (2939, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1738490156000,\"id\":39,\"path\":\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"size\":2855908,\"sortOrder\":1,\"type\":\"image/png\",\"userId\":1},{\"createTime\":1740337172000,\"id\":40,\"path\":\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"size\":3713281,\"sortOrder\":2,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 1, '后台获取所有前台首页Banner图片', '2025-02-24 03:11:37', '2025-02-24 03:11:37', 0);
INSERT INTO `sys_log` VALUES (2940, '信息管理', '新增', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.uploadArticleImage()', '[org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@167686a2]', 'POST', NULL, '{\"code\":200,\"data\":{\"createTime\":1740337928239,\"id\":41,\"path\":\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\",\"size\":4203560,\"sortOrder\":3,\"type\":\"image/png\",\"userId\":1},\"msg\":\"success\",\"timestamp\":0}', '/banners/upload/banner', 1669, '添加前台首页Banner图片', '2025-02-24 03:12:08', '2025-02-24 03:12:08', 0);
INSERT INTO `sys_log` VALUES (2941, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1738490156000,\"id\":39,\"path\":\"http://minioapi.overthinker.top/cloud/banners/80e6a91c-5f31-4810-9aea-9623de7409cc.png\",\"size\":2855908,\"sortOrder\":1,\"type\":\"image/png\",\"userId\":1},{\"createTime\":1740337172000,\"id\":40,\"path\":\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"size\":3713281,\"sortOrder\":2,\"type\":\"image/png\",\"userId\":1},{\"createTime\":1740337928000,\"id\":41,\"path\":\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\",\"size\":4203560,\"sortOrder\":3,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 1, '后台获取所有前台首页Banner图片', '2025-02-24 03:12:08', '2025-02-24 03:12:08', 0);
INSERT INTO `sys_log` VALUES (2942, '信息管理', '删除', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.delete()', '[39]', 'DELETE', NULL, '{\"code\":200,\"data\":\"删除成功\",\"msg\":\"success\",\"timestamp\":0}', '/banners/39', 222, '删除前台首页Banner图片', '2025-02-24 03:12:10', '2025-02-24 03:12:10', 0);
INSERT INTO `sys_log` VALUES (2943, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1740337172000,\"id\":40,\"path\":\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"size\":3713281,\"sortOrder\":2,\"type\":\"image/png\",\"userId\":1},{\"createTime\":1740337928000,\"id\":41,\"path\":\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\",\"size\":4203560,\"sortOrder\":3,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 1, '后台获取所有前台首页Banner图片', '2025-02-24 03:12:10', '2025-02-24 03:12:10', 0);
INSERT INTO `sys_log` VALUES (2944, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1740337172000,\"id\":40,\"path\":\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"size\":3713281,\"sortOrder\":2,\"type\":\"image/png\",\"userId\":1},{\"createTime\":1740337928000,\"id\":41,\"path\":\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\",\"size\":4203560,\"sortOrder\":3,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 1, '后台获取所有前台首页Banner图片', '2025-02-24 03:12:13', '2025-02-24 03:12:13', 0);
INSERT INTO `sys_log` VALUES (2945, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 03:12:21', '2025-02-24 03:12:21', 0);
INSERT INTO `sys_log` VALUES (2946, '邮件发送', '邮件发送', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PublicController.askVerifyCode()', '[\"overhyyds@outlook.com\",\"register\"]', 'GET', NULL, '{\"code\":200,\"data\":\"验证码已发送，请注意查收！\",\"msg\":\"success\",\"timestamp\":0}', '/public/ask-code', 4020, '邮件发送', '2025-02-24 03:13:21', '2025-02-24 03:13:21', 0);
INSERT INTO `sys_log` VALUES (2947, '前台注册', '新增', 'unknown-1702606997', '127.0.0.1', '内网IP', 2, 'com.overthinker.cloud.web.controller.UserController.register()', '[{\"code\":\"141236\",\"email\":\"overhyyds@outlook.com\",\"password\":\"123456\",\"username\":\"abcd\"}]', 'POST', '\r\n### Error updating database.  Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\r\n### The error may exist in com/overthinker/cloud/web/mapper/UserMapper.java (best guess)\r\n### The error may involve com.overthinker.cloud.web.mapper.UserMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO sys_user  ( nickname, username, password, gender, avatar, intro, email, register_type, register_ip,      login_time, create_time, update_time, is_deleted )  VALUES (  ?, ?, ?, ?, ?, ?, ?, ?, ?,      ?, ?, ?, ?  )\r\n### Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\n; Field \'register_address\' doesn\'t have a default value', NULL, '/user/register', 19226, NULL, '2025-02-24 03:14:32', '2025-02-24 03:14:32', 0);
INSERT INTO `sys_log` VALUES (2948, '前台注册', '新增', 'unknown-1702606997', '127.0.0.1', '内网IP', 2, 'com.overthinker.cloud.web.controller.UserController.register()', '[{\"code\":\"141236\",\"email\":\"overhyyds@outlook.com\",\"password\":\"123456\",\"username\":\"abcd\"}]', 'POST', '\r\n### Error updating database.  Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\r\n### The error may exist in com/overthinker/cloud/web/mapper/UserMapper.java (best guess)\r\n### The error may involve com.overthinker.cloud.web.mapper.UserMapper.insert-Inline\r\n### The error occurred while setting parameters\r\n### SQL: INSERT INTO sys_user  ( nickname, username, password, gender, avatar, intro, email, register_type, register_ip,      login_time, create_time, update_time, is_deleted )  VALUES (  ?, ?, ?, ?, ?, ?, ?, ?, ?,      ?, ?, ?, ?  )\r\n### Cause: java.sql.SQLException: Field \'register_address\' doesn\'t have a default value\n; Field \'register_address\' doesn\'t have a default value', NULL, '/user/register', 1895, NULL, '2025-02-24 03:15:10', '2025-02-24 03:15:10', 0);
INSERT INTO `sys_log` VALUES (2949, '前台注册', '新增', 'unknown-1702606997', '127.0.0.1', '内网IP', 1, 'com.overthinker.cloud.web.controller.UserController.register()', '[{\"code\":\"141236\",\"email\":\"overhyyds@outlook.com\",\"password\":\"123456\",\"username\":\"abcd\"}]', 'POST', NULL, '{\"code\":1005,\"msg\":\"请先获取验证码\",\"timestamp\":0}', '/user/register', 16, '用户注册', '2025-02-24 03:19:21', '2025-02-24 03:19:21', 0);
INSERT INTO `sys_log` VALUES (2950, '前台注册', '新增', 'unknown-1702606997', '127.0.0.1', '内网IP', 1, 'com.overthinker.cloud.web.controller.UserController.register()', '[{\"code\":\"141236\",\"email\":\"overhyyds@outlook.com\",\"password\":\"123456\",\"username\":\"abcd\"}]', 'POST', NULL, '{\"code\":1005,\"msg\":\"请先获取验证码\",\"timestamp\":0}', '/user/register', 4, '用户注册', '2025-02-24 03:19:35', '2025-02-24 03:19:35', 0);
INSERT INTO `sys_log` VALUES (2951, '邮件发送', '邮件发送', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PublicController.askVerifyCode()', '[\"overhyyds@outlook.com\",\"register\"]', 'GET', NULL, '{\"code\":200,\"data\":\"验证码已发送，请注意查收！\",\"msg\":\"success\",\"timestamp\":0}', '/public/ask-code', 99, '邮件发送', '2025-02-24 03:19:37', '2025-02-24 03:19:37', 0);
INSERT INTO `sys_log` VALUES (2952, '前台注册', '新增', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.UserController.register()', '[{\"code\":\"714089\",\"email\":\"overhyyds@outlook.com\",\"password\":\"123456\",\"username\":\"abcd\"}]', 'POST', NULL, '{\"code\":200,\"msg\":\"success\",\"timestamp\":0}', '/user/register', 302, '用户注册', '2025-02-24 03:19:48', '2025-02-24 03:19:48', 0);
INSERT INTO `sys_log` VALUES (2953, '信息管理', '获取', 'abcd', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 4, '前台获取所有前台首页Banner图片', '2025-02-24 03:20:00', '2025-02-24 03:20:00', 0);
INSERT INTO `sys_log` VALUES (2954, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1740337172000,\"id\":40,\"path\":\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"size\":3713281,\"sortOrder\":2,\"type\":\"image/png\",\"userId\":1},{\"createTime\":1740337928000,\"id\":41,\"path\":\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\",\"size\":4203560,\"sortOrder\":3,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 2, '后台获取所有前台首页Banner图片', '2025-02-24 03:22:23', '2025-02-24 03:22:23', 0);
INSERT INTO `sys_log` VALUES (2955, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1740337172000,\"id\":40,\"path\":\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"size\":3713281,\"sortOrder\":2,\"type\":\"image/png\",\"userId\":1},{\"createTime\":1740337928000,\"id\":41,\"path\":\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\",\"size\":4203560,\"sortOrder\":3,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 2, '后台获取所有前台首页Banner图片', '2025-02-24 03:22:34', '2025-02-24 03:22:34', 0);
INSERT INTO `sys_log` VALUES (2956, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1740337172000,\"id\":40,\"path\":\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"size\":3713281,\"sortOrder\":2,\"type\":\"image/png\",\"userId\":1},{\"createTime\":1740337928000,\"id\":41,\"path\":\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\",\"size\":4203560,\"sortOrder\":3,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 1, '后台获取所有前台首页Banner图片', '2025-02-24 03:22:57', '2025-02-24 03:22:57', 0);
INSERT INTO `sys_log` VALUES (2957, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 3, '前台获取所有前台首页Banner图片', '2025-02-24 03:23:22', '2025-02-24 03:23:22', 0);
INSERT INTO `sys_log` VALUES (2958, '相册管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.backList()', '[1,10,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/back/list', 45, '后台相册或照片列表', '2025-02-24 03:25:18', '2025-02-24 03:25:18', 0);
INSERT INTO `sys_log` VALUES (2959, '相册管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.backList()', '[1,10,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/back/list', 15, '后台相册或照片列表', '2025-02-24 03:25:18', '2025-02-24 03:25:18', 0);
INSERT INTO `sys_log` VALUES (2960, '信息管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.backGetBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"createTime\":1740337172000,\"id\":40,\"path\":\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"size\":3713281,\"sortOrder\":2,\"type\":\"image/png\",\"userId\":1},{\"createTime\":1740337928000,\"id\":41,\"path\":\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\",\"size\":4203560,\"sortOrder\":3,\"type\":\"image/png\",\"userId\":1}],\"msg\":\"success\",\"timestamp\":0}', '/banners/back/list', 1, '后台获取所有前台首页Banner图片', '2025-02-24 03:25:19', '2025-02-24 03:25:19', 0);
INSERT INTO `sys_log` VALUES (2961, '文章管理', '获取', 'ADMIN', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.listArticle()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/8b4eb727-09b8-457d-a710-a1f672a29316.png\",\"articleTitle\":\"未来城市\",\"articleType\":1,\"categoryId\":14,\"categoryName\":\"科技\",\"createTime\":1740336256000,\"id\":47,\"isTop\":0,\"status\":1,\"tagsName\":[\"社会\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/e2d1ec3d-5e18-4e42-9fa4-cce9c4d2a1f5.png\",\"articleTitle\":\"探索未来教育的创新之路\",\"articleType\":1,\"categoryId\":13,\"categoryName\":\"生活\",\"createTime\":1740335378000,\"id\":46,\"isTop\":0,\"status\":1,\"tagsName\":[\"社会\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/3655f106-2452-4403-91bc-ac65b3cfe8c7.png\",\"articleTitle\":\"未来工作与职业发展的新趋势\",\"articleType\":1,\"categoryId\":14,\"categoryName\":\"科技\",\"createTime\":1740335298000,\"id\":45,\"isTop\":0,\"status\":1,\"tagsName\":[\"社会\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/b8b2721c-2d45-4bc5-8d2e-81c1bd5ffca8.png\",\"articleTitle\":\"城市绿化\",\"articleType\":1,\"categoryId\":13,\"categoryName\":\"生活\",\"createTime\":1740335146000,\"id\":44,\"isTop\":1,\"status\":1,\"tagsName\":[\"社会\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/1ea84532-cdb1-4b84-8c05-a523d184aee6.png\",\"articleTitle\":\"探索未知，塑造未来\",\"articleType\":1,\"categoryId\":14,\"categoryName\":\"科技\",\"createTime\":1740333977000,\"id\":43,\"isTop\":1,\"status\":1,\"tagsName\":[\"人工智能\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/530d1a23-a1b7-4117-98b2-a8a8c4f28293.png\",\"articleTitle\":\"一场充满惊喜与挑战的旅程\",\"articleType\":1,\"categoryId\":13,\"categoryName\":\"生活\",\"createTime\":1740333770000,\"id\":42,\"isTop\":1,\"status\":1,\"tagsName\":[\"测试标签\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://minioapi.overthinker.top/cloud/article/articleCover/41455699-438b-4e36-aa3f-eec094383386.png\",\"articleTitle\":\"你好\",\"articleType\":1,\"categoryId\":13,\"categoryName\":\"生活\",\"createTime\":1740307295000,\"id\":41,\"isTop\":0,\"status\":1,\"tagsName\":[\"测试标签\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0}],\"msg\":\"success\",\"timestamp\":0}', '/article/back/list', 38, '获取所有的文章列表', '2025-02-24 03:25:22', '2025-02-24 03:25:22', 0);
INSERT INTO `sys_log` VALUES (2962, '信息管理', '获取', 'abcd', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 03:25:53', '2025-02-24 03:25:53', 0);
INSERT INTO `sys_log` VALUES (2963, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 5, '前台获取所有前台首页Banner图片', '2025-02-24 22:02:24', '2025-02-24 22:02:24', 0);
INSERT INTO `sys_log` VALUES (2964, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 23, '前台相册或照片列表', '2025-02-24 22:02:49', '2025-02-24 22:02:49', 0);
INSERT INTO `sys_log` VALUES (2965, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 3, '前台获取所有前台首页Banner图片', '2025-02-24 22:10:41', '2025-02-24 22:10:41', 0);
INSERT INTO `sys_log` VALUES (2966, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 22:10:58', '2025-02-24 22:10:58', 0);
INSERT INTO `sys_log` VALUES (2967, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 22:11:09', '2025-02-24 22:11:09', 0);
INSERT INTO `sys_log` VALUES (2968, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 22:11:19', '2025-02-24 22:11:19', 0);
INSERT INTO `sys_log` VALUES (2969, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 22:11:33', '2025-02-24 22:11:33', 0);
INSERT INTO `sys_log` VALUES (2970, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 22:29:55', '2025-02-24 22:29:55', 0);
INSERT INTO `sys_log` VALUES (2971, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 12, '前台相册或照片列表', '2025-02-24 22:30:42', '2025-02-24 22:30:42', 0);
INSERT INTO `sys_log` VALUES (2972, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 6, '前台获取所有前台首页Banner图片', '2025-02-24 22:31:30', '2025-02-24 22:31:30', 0);
INSERT INTO `sys_log` VALUES (2973, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 12, '前台相册或照片列表', '2025-02-24 22:31:54', '2025-02-24 22:31:54', 0);
INSERT INTO `sys_log` VALUES (2974, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 22:32:00', '2025-02-24 22:32:00', 0);
INSERT INTO `sys_log` VALUES (2975, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 10, '前台相册或照片列表', '2025-02-24 22:32:01', '2025-02-24 22:32:01', 0);
INSERT INTO `sys_log` VALUES (2976, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 22:32:04', '2025-02-24 22:32:04', 0);
INSERT INTO `sys_log` VALUES (2977, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 22:32:34', '2025-02-24 22:32:34', 0);
INSERT INTO `sys_log` VALUES (2978, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 22:32:50', '2025-02-24 22:32:50', 0);
INSERT INTO `sys_log` VALUES (2979, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 22:35:01', '2025-02-24 22:35:01', 0);
INSERT INTO `sys_log` VALUES (2980, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 22:53:40', '2025-02-24 22:53:40', 0);
INSERT INTO `sys_log` VALUES (2981, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 226, '前台获取所有前台首页Banner图片', '2025-02-24 23:00:46', '2025-02-24 23:00:46', 0);
INSERT INTO `sys_log` VALUES (2982, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 3, '前台获取所有前台首页Banner图片', '2025-02-24 23:00:58', '2025-02-24 23:00:58', 0);
INSERT INTO `sys_log` VALUES (2983, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 23:03:58', '2025-02-24 23:03:58', 0);
INSERT INTO `sys_log` VALUES (2984, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 18, '前台获取所有前台首页Banner图片', '2025-02-24 23:11:16', '2025-02-24 23:11:16', 0);
INSERT INTO `sys_log` VALUES (2985, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 15, '前台相册或照片列表', '2025-02-24 23:11:40', '2025-02-24 23:11:40', 0);
INSERT INTO `sys_log` VALUES (2986, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 9, '前台相册或照片列表', '2025-02-24 23:11:42', '2025-02-24 23:11:42', 0);
INSERT INTO `sys_log` VALUES (2987, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 10, '前台相册或照片列表', '2025-02-24 23:11:43', '2025-02-24 23:11:43', 0);
INSERT INTO `sys_log` VALUES (2988, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 16, '前台相册或照片列表', '2025-02-24 23:12:00', '2025-02-24 23:12:00', 0);
INSERT INTO `sys_log` VALUES (2989, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 23:12:04', '2025-02-24 23:12:04', 0);
INSERT INTO `sys_log` VALUES (2990, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-24 23:18:34', '2025-02-24 23:18:34', 0);
INSERT INTO `sys_log` VALUES (2991, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 71, '前台获取所有前台首页Banner图片', '2025-02-24 23:19:56', '2025-02-24 23:19:56', 0);
INSERT INTO `sys_log` VALUES (2992, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 23:35:27', '2025-02-24 23:35:27', 0);
INSERT INTO `sys_log` VALUES (2993, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 23:36:07', '2025-02-24 23:36:07', 0);
INSERT INTO `sys_log` VALUES (2994, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 23:36:09', '2025-02-24 23:36:09', 0);
INSERT INTO `sys_log` VALUES (2995, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 10, '前台相册或照片列表', '2025-02-24 23:42:20', '2025-02-24 23:42:20', 0);
INSERT INTO `sys_log` VALUES (2996, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 23:42:21', '2025-02-24 23:42:21', 0);
INSERT INTO `sys_log` VALUES (2997, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 8, '前台相册或照片列表', '2025-02-24 23:42:25', '2025-02-24 23:42:25', 0);
INSERT INTO `sys_log` VALUES (2998, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-24 23:47:11', '2025-02-24 23:47:11', 0);
INSERT INTO `sys_log` VALUES (2999, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 9, '前台相册或照片列表', '2025-02-25 00:05:42', '2025-02-25 00:05:42', 0);
INSERT INTO `sys_log` VALUES (3000, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,152]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292930000,\"id\":153,\"isCheck\":1,\"name\":\"OIP-C\",\"parentId\":152,\"size\":0.02,\"type\":2,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 8, '前台相册或照片列表', '2025-02-25 00:05:43', '2025-02-25 00:05:43', 0);
INSERT INTO `sys_log` VALUES (3001, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 9, '前台相册或照片列表', '2025-02-25 00:05:53', '2025-02-25 00:05:53', 0);
INSERT INTO `sys_log` VALUES (3002, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 31, '前台相册或照片列表', '2025-02-25 00:08:55', '2025-02-25 00:08:55', 0);
INSERT INTO `sys_log` VALUES (3003, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 11, '前台相册或照片列表', '2025-02-25 00:09:06', '2025-02-25 00:09:06', 0);
INSERT INTO `sys_log` VALUES (3004, '相册管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.PhotoController.getList()', '[1,16,null]', 'GET', NULL, '{\"code\":200,\"data\":{\"page\":[{\"createTime\":1740292919000,\"description\":\"test\",\"id\":152,\"isCheck\":1,\"name\":\"minio\",\"type\":1,\"url\":\"http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg\",\"userId\":1}],\"total\":1},\"msg\":\"success\",\"timestamp\":0}', '/photo/list', 16, '前台相册或照片列表', '2025-02-25 00:09:19', '2025-02-25 00:09:19', 0);
INSERT INTO `sys_log` VALUES (3005, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-25 00:13:10', '2025-02-25 00:13:10', 0);
INSERT INTO `sys_log` VALUES (3006, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 1, '前台获取所有前台首页Banner图片', '2025-02-25 00:13:29', '2025-02-25 00:13:29', 0);
INSERT INTO `sys_log` VALUES (3007, '信息管理', '获取', 'unknown-1702606997', '127.0.0.1', '内网IP', 0, 'com.overthinker.cloud.web.controller.BannersController.getBanners()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[\"http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png\",\"http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png\"],\"msg\":\"success\",\"timestamp\":0}', '/banners/list', 2, '前台获取所有前台首页Banner图片', '2025-02-25 00:14:24', '2025-02-25 00:14:24', 0);

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志编号',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名称',
  `ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录ip',
  `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录地址',
  `browser` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '浏览器',
  `os` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作系统',
  `type` tinyint(1) NOT NULL COMMENT '登录类型(0：前台，1：后台，2：非法登录)',
  `state` tinyint(1) NOT NULL COMMENT '登录状态(0：成功，1：失败)',
  `message` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '登录信息',
  `create_time` datetime NOT NULL COMMENT '用户创建时间',
  `update_time` datetime NOT NULL COMMENT '用户更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 464 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '绑定的哪个组件，默认自带的组件类型分别是：Iframe、RouteView和ComponentError',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父菜单重定向地址(默认第一个子菜单)',
  `affix` tinyint NOT NULL DEFAULT 0 COMMENT '是否是固定页签(0否 1是)',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父级菜单的id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '同路由中的name，主要是用于保活的左右',
  `hide_in_menu` tinyint NOT NULL DEFAULT 0 COMMENT '是否隐藏当前菜单(0否 1是)',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '如果当前是iframe的模式，需要有一个跳转的url支撑，其不能和path重复，path还是为路由',
  `hide_in_breadcrumb` tinyint NOT NULL DEFAULT 1 COMMENT '是否存在于面包屑(0否 1是)',
  `hide_children_in_menu` tinyint NOT NULL DEFAULT 1 COMMENT '是否不需要显示所有的子菜单(0否 1是)',
  `keep_alive` tinyint NOT NULL DEFAULT 1 COMMENT '是否保活(0否 1是)',
  `target` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '全连接跳转模式(\'_blank\' | \'_self\' | \'_parent\')',
  `is_disable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用 (0否 1是)',
  `order_num` int NOT NULL DEFAULT 1 COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 75 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 'SettingTwoTone', '/system', 'RouteView', '/system/menu', 0, NULL, 'System', 0, NULL, 1, 0, 1, NULL, 0, 2, '2023-11-17 14:49:02', '2023-11-29 17:33:13', 0);
INSERT INTO `sys_menu` VALUES (2, '菜单管理', 'MenuOutlined', '/system/menu', '/system/menu', '', 0, 1, 'Menu', 0, NULL, 1, 0, 1, NULL, 0, 1, '2023-11-17 14:49:02', '2023-11-28 17:27:43', 0);
INSERT INTO `sys_menu` VALUES (3, '用户管理', 'UserOutlined', '/system/user', '/system/user', '', 0, 1, 'User', 0, NULL, 1, 0, 1, NULL, 0, 0, '2023-11-17 14:49:02', '2023-11-29 14:46:27', 0);
INSERT INTO `sys_menu` VALUES (21, '首页', 'HomeTwoTone', '/welcome', '/welcome', '', 0, NULL, NULL, 0, NULL, 1, 1, 1, '', 0, 0, '2023-11-28 16:36:33', '2023-11-28 19:57:13', 0);
INSERT INTO `sys_menu` VALUES (23, '角色管理', 'TeamOutlined', '/system/role', '/system/role', NULL, 0, 1, NULL, 0, NULL, 1, 1, 1, '', 0, 3, '2023-11-29 15:41:30', '2023-12-04 12:16:00', 0);
INSERT INTO `sys_menu` VALUES (24, '权限管理', 'UnlockOutlined', '/system/permission', '/system/permission', '', 0, 1, NULL, 0, NULL, 1, 1, 1, '', 0, 4, '2023-11-29 17:13:50', '2023-11-29 17:14:10', 0);
INSERT INTO `sys_menu` VALUES (25, '日志管理', 'HighlightOutlined', '/log', 'RouteView', '/log/operate', 0, 1, NULL, 0, '', 1, 1, 1, '', 0, 5, '2023-11-29 17:17:29', '2023-11-29 17:17:29', 0);
INSERT INTO `sys_menu` VALUES (26, '操作日志', 'FileProtectOutlined', '/log/operate', '/system/log/operate', '', 0, 25, NULL, 0, NULL, 1, 1, 1, '', 0, 0, '2023-11-29 17:20:28', '2023-11-29 17:29:20', 0);
INSERT INTO `sys_menu` VALUES (27, '登录日志', 'SolutionOutlined', '/log/login', '/system/log/login', '', 0, 25, NULL, 0, '', 1, 1, 1, '', 0, 1, '2023-11-29 17:29:02', '2023-11-29 17:29:02', 0);
INSERT INTO `sys_menu` VALUES (28, '网站管理', 'AppstoreTwoTone', '/blog', 'RouteView', NULL, 0, NULL, NULL, 0, NULL, 1, 1, 1, '', 0, 1, '2023-11-29 17:34:17', '2023-12-25 23:50:05', 0);
INSERT INTO `sys_menu` VALUES (29, '信息管理', 'ReadOutlined', '/blog/info', '/blog/info', '', 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 0, '2023-11-29 20:05:20', '2023-11-29 20:09:38', 0);
INSERT INTO `sys_menu` VALUES (30, '文章管理', 'FormOutlined', '/blog/essay', '', NULL, 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 0, '2023-11-29 20:11:25', '2023-12-26 11:16:20', 0);
INSERT INTO `sys_menu` VALUES (31, '发布文章', 'SendOutlined', '/blog/essay/publish', '/blog/essay/publish', '', 0, 30, NULL, 0, '', 1, 1, 1, '', 0, 0, '2023-11-29 20:13:00', '2023-11-29 20:13:00', 0);
INSERT INTO `sys_menu` VALUES (32, '文章列表', 'OrderedListOutlined', '/blog/essay/list', '/blog/essay/list', '', 0, 30, NULL, 0, '', 1, 1, 1, '', 0, 0, '2023-11-29 20:14:13', '2023-11-29 20:14:13', 0);
INSERT INTO `sys_menu` VALUES (33, '标签管理', 'TagsOutlined', '/blog/tag', '/blog/tag', '', 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 2, '2023-11-29 20:15:13', '2023-11-29 20:20:28', 0);
INSERT INTO `sys_menu` VALUES (34, '分类管理', 'ContainerOutlined', '/blog/category', '/blog/category', '', 0, 28, NULL, 0, '', 1, 1, 1, '', 0, 3, '2023-11-29 20:19:09', '2023-11-29 20:19:09', 0);
INSERT INTO `sys_menu` VALUES (35, '评论管理', 'CommentOutlined', '/blog/comment', '/blog/comment', '', 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 4, '2023-11-29 20:21:48', '2023-11-29 20:22:06', 0);
INSERT INTO `sys_menu` VALUES (36, '留言管理', 'ScheduleOutlined', '/blog/message', '/blog/message', NULL, 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 1, '2023-11-29 20:23:19', '2023-12-26 11:16:24', 0);
INSERT INTO `sys_menu` VALUES (37, '树洞管理', 'BulbOutlined', '/blog/tree-hole', '/blog/tree-hole', '', 0, 28, NULL, 0, '', 1, 1, 1, '', 0, 5, '2023-11-29 20:27:40', '2023-11-29 20:27:40', 0);
INSERT INTO `sys_menu` VALUES (38, '聊天管理', 'RobotOutlined', '/blog/gpt', '/blog/gpt', '', 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 6, '2023-11-29 20:29:08', '2023-11-29 20:29:27', 0);
INSERT INTO `sys_menu` VALUES (39, '友链管理', 'NodeIndexOutlined', '/blog/link', '/blog/link', NULL, 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 6, '2023-11-29 20:31:25', '2024-01-22 20:27:26', 0);
INSERT INTO `sys_menu` VALUES (43, '收藏管理', 'InboxOutlined', '/blog/collect', '/blog/collect', NULL, 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 3, '2023-11-29 20:54:15', '2023-11-29 20:54:47', 0);
INSERT INTO `sys_menu` VALUES (44, '服务监控', 'AlertOutlined', '/system/server-monitoring', '/system/server-monitoring', NULL, 0, 1, NULL, 0, NULL, 1, 1, 1, '', 0, 6, '2023-11-29 21:01:24', '2023-12-14 15:26:34', 0);
INSERT INTO `sys_menu` VALUES (64, '角色授权', '', '/role/authorization', '/system/role/user-role', NULL, 0, 1, NULL, 1, NULL, 1, 1, 1, '', 0, 0, '2023-12-04 12:07:00', '2023-12-05 09:57:09', 0);
INSERT INTO `sys_menu` VALUES (65, '权限授权', '', '/permission/authorization', '/system/permission/role-permission', NULL, 0, 1, NULL, 1, NULL, 1, 1, 1, '', 0, 0, '2023-12-07 14:38:45', '2023-12-07 14:41:44', 0);
INSERT INTO `sys_menu` VALUES (68, '用户授权', '', '/user/role', '/system/user/role-user', NULL, 0, 1, NULL, 1, NULL, 1, 1, 1, '', 0, 0, '2023-12-19 10:37:05', '2023-12-19 10:38:16', 0);
INSERT INTO `sys_menu` VALUES (70, '跳转前台', 'TabletTwoTone', 'http://baidu.com', NULL, NULL, 0, NULL, NULL, 1, '', 1, 1, 1, '_blank', 0, 6, '2024-01-22 20:38:54', '2025-02-24 01:11:00', 0);
INSERT INTO `sys_menu` VALUES (71, '视频管理', 'PlayCircleOutlined', '/cms/video', 'RouteView', NULL, 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 0, '2025-02-12 00:18:42', '2025-02-12 00:20:33', 0);
INSERT INTO `sys_menu` VALUES (72, '上传视频', 'UpCircleOutlined', '/cms/video/publish', '/cms/video/publish', '', 0, 71, NULL, 0, '', 1, 1, 1, '', 0, 0, '2025-02-12 00:20:10', '2025-02-12 00:20:10', 0);
INSERT INTO `sys_menu` VALUES (73, '分析页', 'FundTwoTone', '/cms/analysis', '/cms/analysis', '', 0, 1, NULL, 0, '', 1, 1, 1, '', 0, 0, '2025-02-16 22:39:46', '2025-02-16 22:39:46', 0);
INSERT INTO `sys_menu` VALUES (74, '图片管理', 'InstagramOutlined', '/photo', '/blog/photo', '', 0, 28, NULL, 0, '', 1, 1, 1, '', 0, 0, '2025-02-23 14:41:39', '2025-02-23 14:41:39', 0);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '权限表id',
  `permission_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '描述',
  `permission_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限字符',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 149 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES (5, '获取菜单', 'system:menu:list', 2, '2023-12-06 08:41:49', '2023-12-14 17:21:57', 0);
INSERT INTO `sys_permission` VALUES (6, '查询菜单', 'system:menu:select', 2, '2023-12-05 08:41:54', '2023-12-07 12:02:31', 0);
INSERT INTO `sys_permission` VALUES (7, '修改菜单', 'system:menu:update', 2, '2023-12-04 08:41:54', '2023-12-12 20:36:49', 0);
INSERT INTO `sys_permission` VALUES (8, '删除菜单', 'system:menu:delete', 2, '2023-12-04 08:41:54', '2023-12-11 21:03:10', 0);
INSERT INTO `sys_permission` VALUES (9, '添加菜单', 'system:menu:add', 2, '2023-12-02 08:41:54', '2023-12-04 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (10, '修改菜单角色列表', 'system:menu:role:list', 2, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (11, '获取所有角色', 'system:role:list', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (12, '更新角色状态', 'system:role:status:update', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (13, '获取对应角色信息', 'system:role:get', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (14, '修改角色信息', 'system:role:update', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (15, '根据id删除角色', 'system:role:delete', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (16, '根据条件搜索角色', 'system:role:search', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (17, '查询拥有角色的用户列表', 'system:user:role:list', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (18, '查询未拥有角色的用户列表', 'system:not:role:user:list', 23, '2023-12-02 08:41:54', '2023-12-06 10:43:49', 0);
INSERT INTO `sys_permission` VALUES (19, '添加用户角色关系', 'system:user:role:add', 23, '2023-12-05 08:41:54', '2023-12-11 21:02:51', 0);
INSERT INTO `sys_permission` VALUES (20, '删除用户角色关系', 'system:user:role:delete', 23, '2023-12-02 08:41:54', '2023-12-02 08:41:54', 0);
INSERT INTO `sys_permission` VALUES (21, '所有权限', 'system:permission:list', 24, '2023-12-06 10:34:21', '2023-12-06 10:34:24', 0);
INSERT INTO `sys_permission` VALUES (22, '查询所有权限所在菜单', 'system:permission:menu:list', 24, '2023-12-06 14:26:28', '2023-12-06 14:26:31', 0);
INSERT INTO `sys_permission` VALUES (23, '搜索权限', 'system:permission:search', 24, '2023-12-06 15:18:29', '2023-12-06 15:18:33', 0);
INSERT INTO `sys_permission` VALUES (24, '添加权限', 'system:permission:add', 24, '2023-12-06 19:12:47', '2023-12-06 19:12:50', 0);
INSERT INTO `sys_permission` VALUES (25, '测试测试', 'sdss', 3, '2023-12-06 20:26:46', '2023-12-06 20:26:46', 1);
INSERT INTO `sys_permission` VALUES (26, 'sdsd', 'sdd', 26, '2023-12-06 20:29:24', '2023-12-06 20:29:24', 1);
INSERT INTO `sys_permission` VALUES (27, '获取要修改的权限信息', 'system:permission:get', 24, '2023-12-06 20:48:33', '2023-12-06 20:48:33', 0);
INSERT INTO `sys_permission` VALUES (28, '修改权限字符信息', 'system:permission:update', 24, '2023-12-07 12:01:34', '2023-12-07 12:01:36', 0);
INSERT INTO `sys_permission` VALUES (29, 'sss', '测试', 21, '2023-12-07 12:06:26', '2023-12-07 12:06:26', 1);
INSERT INTO `sys_permission` VALUES (30, '删除权限', 'system:permission:delete', 24, '2023-12-07 12:14:14', '2023-12-07 12:14:14', 0);
INSERT INTO `sys_permission` VALUES (31, '查询权限的角色列表', 'system:permission:role:list', 65, '2023-12-07 15:02:03', '2023-12-07 15:02:03', 0);
INSERT INTO `sys_permission` VALUES (32, '2121', '测试', 26, '2023-12-07 17:17:13', '2023-12-07 17:17:13', 1);
INSERT INTO `sys_permission` VALUES (33, '查询没有该权限的角色列表', 'system:permission:role:not:list', 65, '2023-12-07 17:41:38', '2023-12-07 17:41:38', 0);
INSERT INTO `sys_permission` VALUES (34, '单个/批量添加角色权限关系', 'system:permission:role:add', 65, '2023-12-07 20:53:14', '2023-12-08 10:51:10', 0);
INSERT INTO `sys_permission` VALUES (35, '删除角色权限关系', 'system:permission:role:delete', 65, '2023-12-07 21:00:55', '2023-12-07 21:00:55', 0);
INSERT INTO `sys_permission` VALUES (36, '显示所有登录日志', 'system:log:login:list', 27, '2023-12-11 16:20:00', '2023-12-14 17:48:07', 0);
INSERT INTO `sys_permission` VALUES (37, '登录日志搜索', 'system:log:login:search', 27, '2023-12-11 19:51:27', '2023-12-11 19:51:27', 0);
INSERT INTO `sys_permission` VALUES (38, '删除/清空登录日志', 'system:log:login:delete', 27, '2023-12-11 20:19:08', '2023-12-11 20:19:08', 0);
INSERT INTO `sys_permission` VALUES (45, '显示所有操作日志', 'system:log:list', 26, '2023-12-13 16:13:41', '2023-12-13 16:13:41', 0);
INSERT INTO `sys_permission` VALUES (46, '显示所有操作日志', 'system:log:list', 26, '2023-12-13 16:13:41', '2023-12-13 16:13:41', 1);
INSERT INTO `sys_permission` VALUES (87, '添加角色信息', 'system:role:add', 23, '2023-12-13 17:23:42', '2023-12-13 17:23:42', 0);
INSERT INTO `sys_permission` VALUES (88, '添加角色信息', 'system:role:add', 23, '2023-12-13 17:23:42', '2023-12-13 17:23:42', 1);
INSERT INTO `sys_permission` VALUES (91, '搜索操作日志', 'system:log:search', 26, '2023-12-13 20:43:04', '2023-12-13 20:43:04', 0);
INSERT INTO `sys_permission` VALUES (92, '删除/清空操作日志', 'system:log:delete', 26, '2023-12-14 08:45:38', '2023-12-14 08:45:38', 0);
INSERT INTO `sys_permission` VALUES (93, 'id查询操作日志', 'system:log:select:id', 26, '2023-12-14 09:00:53', '2023-12-14 09:00:53', 0);
INSERT INTO `sys_permission` VALUES (94, '获取服务监控数据', 'monitor:server:list', 44, '2023-12-14 15:21:21', '2023-12-14 15:21:21', 0);
INSERT INTO `sys_permission` VALUES (95, 'ssss', 'system:menu:list	', 26, '2023-12-14 17:19:50', '2023-12-14 17:19:50', 1);
INSERT INTO `sys_permission` VALUES (96, 'dd', 'system:menu:lists', 21, '2023-12-14 17:21:46', '2023-12-14 17:21:46', 1);
INSERT INTO `sys_permission` VALUES (97, '获取用户列表', 'system:user:list', 3, '2023-12-18 12:07:00', '2023-12-18 12:07:00', 0);
INSERT INTO `sys_permission` VALUES (98, '搜索用户列表', 'system:user:search', 3, '2023-12-18 14:15:46', '2023-12-18 14:15:46', 0);
INSERT INTO `sys_permission` VALUES (99, '更新用户状态', 'system:user:status:update', 3, '2023-12-18 15:11:34', '2023-12-18 15:11:34', 0);
INSERT INTO `sys_permission` VALUES (100, '获取用户详细信息', 'system:user:details', 3, '2023-12-18 16:40:52', '2023-12-18 16:40:52', 0);
INSERT INTO `sys_permission` VALUES (101, '删除用户&用户角色关系', 'system:user:delete', 3, '2023-12-19 10:11:46', '2023-12-19 10:12:15', 0);
INSERT INTO `sys_permission` VALUES (102, '查询没有该用户的角色列表', 'system:user:role:not:list', 23, '2023-12-19 11:10:11', '2023-12-19 11:10:11', 0);
INSERT INTO `sys_permission` VALUES (103, '查询拥有用户的角色列表', 'system:role:user:list', 23, '2023-12-19 11:17:55', '2023-12-19 11:17:55', 0);
INSERT INTO `sys_permission` VALUES (104, '搜索管理菜单列表', 'system:search:menu:list', 2, '2023-12-25 11:48:02', '2023-12-25 11:48:02', 0);
INSERT INTO `sys_permission` VALUES (105, '添加或修改站长信息', 'blog:update:websiteInfo', 29, '2023-12-27 16:19:23', '2024-01-04 10:49:12', 0);
INSERT INTO `sys_permission` VALUES (106, '查看网站信息-后台', 'blog:get:websiteInfo', 29, '2023-12-29 08:52:51', '2024-01-22 22:18:56', 0);
INSERT INTO `sys_permission` VALUES (107, '新增标签', 'blog:tag:add', 33, '2024-01-04 10:55:39', '2024-01-04 10:55:39', 0);
INSERT INTO `sys_permission` VALUES (108, '新增分类', 'blog:category:add', 34, '2024-01-04 11:17:12', '2024-01-04 11:17:12', 0);
INSERT INTO `sys_permission` VALUES (109, '发布文章相关', 'blog:publish:article', 31, '2024-01-05 15:01:54', '2024-01-05 15:01:54', 0);
INSERT INTO `sys_permission` VALUES (110, '获取文章列表', 'blog:article:list', 32, '2024-01-07 11:28:14', '2024-01-07 11:28:14', 0);
INSERT INTO `sys_permission` VALUES (111, '搜索文章', 'blog:article:search', 32, '2024-01-07 19:30:10', '2024-01-07 19:30:10', 0);
INSERT INTO `sys_permission` VALUES (112, '修改文章', 'blog:article:update', 32, '2024-01-07 19:56:37', '2024-01-07 19:56:37', 0);
INSERT INTO `sys_permission` VALUES (113, '回显文章数据', 'blog:article:echo', 31, '2024-01-08 09:24:01', '2024-01-08 09:24:01', 0);
INSERT INTO `sys_permission` VALUES (114, '删除文章', 'blog:article:delete', 30, '2024-01-08 11:29:23', '2024-01-08 11:29:23', 0);
INSERT INTO `sys_permission` VALUES (115, '后台留言列表', 'blog:leaveword:list', 36, '2024-01-12 21:14:18', '2024-01-12 21:14:18', 0);
INSERT INTO `sys_permission` VALUES (116, '搜索后台留言列表', 'blog:leaveword:search', 36, '2024-01-16 08:50:46', '2024-01-16 08:50:46', 0);
INSERT INTO `sys_permission` VALUES (117, '修改留言是否通过', 'blog:leaveword:isCheck', 36, '2024-01-16 10:02:20', '2024-01-16 10:02:20', 0);
INSERT INTO `sys_permission` VALUES (118, '删除留言', 'blog:leaveword:delete', 36, '2024-01-16 11:11:59', '2024-01-16 11:11:59', 0);
INSERT INTO `sys_permission` VALUES (119, '获取标签列表', 'blog:tag:list', 33, '2024-01-18 14:30:10', '2024-01-18 14:30:10', 0);
INSERT INTO `sys_permission` VALUES (120, '搜索标签列表', 'blog:tag:search', 33, '2024-01-18 14:47:10', '2024-01-18 14:47:10', 0);
INSERT INTO `sys_permission` VALUES (121, '修改标签', 'blog:tag:update', 33, '2024-01-18 15:56:45', '2024-01-18 15:56:45', 0);
INSERT INTO `sys_permission` VALUES (122, '删除标签', 'blog:tag:delete', 33, '2024-01-18 16:16:41', '2024-01-18 16:16:41', 0);
INSERT INTO `sys_permission` VALUES (123, '获取分类列表', 'blog:category:list', 34, '2024-01-18 22:42:29', '2024-01-18 22:43:28', 0);
INSERT INTO `sys_permission` VALUES (124, '搜索分类列表', 'blog:category:search', 34, '2024-01-18 22:43:06', '2024-01-18 22:43:06', 0);
INSERT INTO `sys_permission` VALUES (125, '修改分类', 'blog:category:update', 34, '2024-01-18 22:44:21', '2024-01-18 22:44:21', 0);
INSERT INTO `sys_permission` VALUES (126, '删除分类', 'blog:category:delete', 34, '2024-01-18 22:44:51', '2024-01-18 22:44:51', 0);
INSERT INTO `sys_permission` VALUES (127, '后台树洞列表', 'blog:treeHole:list', 37, '2024-01-19 21:25:39', '2024-01-19 21:25:39', 0);
INSERT INTO `sys_permission` VALUES (128, '搜索后台树洞列表', 'blog:treeHole:search', 37, '2024-01-19 21:26:03', '2024-01-19 21:26:03', 0);
INSERT INTO `sys_permission` VALUES (129, '修改树洞是否通过', 'blog:treeHole:isCheck', 37, '2024-01-19 21:26:28', '2024-01-19 21:26:28', 0);
INSERT INTO `sys_permission` VALUES (130, '删除树洞', 'blog:treeHole:delete', 37, '2024-01-19 21:27:20', '2024-01-19 21:27:20', 0);
INSERT INTO `sys_permission` VALUES (131, '搜索后台收藏列表', 'blog:favorite:search', 43, '2024-01-21 09:36:25', '2024-01-21 09:36:25', 0);
INSERT INTO `sys_permission` VALUES (132, '修改收藏是否通过', 'blog:favorite:isCheck', 43, '2024-01-21 09:36:47', '2024-01-21 09:36:47', 0);
INSERT INTO `sys_permission` VALUES (133, '删除收藏', 'blog:favorite:delete', 43, '2024-01-21 09:37:11', '2024-01-21 09:37:11', 0);
INSERT INTO `sys_permission` VALUES (134, '后台收藏列表', 'blog:favorite:list', 43, '2024-01-21 09:39:35', '2024-01-21 09:39:35', 0);
INSERT INTO `sys_permission` VALUES (135, '后台留言列表', 'blog:chatGpt:list', 38, '2024-01-21 11:05:47', '2024-01-21 11:05:47', 1);
INSERT INTO `sys_permission` VALUES (136, '后台聊天列表', 'blog:chatGpt:list', 38, '2024-01-21 11:06:39', '2024-01-21 11:06:39', 0);
INSERT INTO `sys_permission` VALUES (137, '搜索后台聊天列表', 'blog:chatGpt:search', 38, '2024-01-21 11:07:18', '2024-01-21 11:07:18', 0);
INSERT INTO `sys_permission` VALUES (138, '修改聊天是否通过', 'blog:chatGpt:isCheck', 38, '2024-01-21 11:07:53', '2024-01-21 11:07:53', 0);
INSERT INTO `sys_permission` VALUES (139, '删除聊天', 'blog:chatGpt:delete', 38, '2024-01-21 11:08:18', '2024-01-21 11:08:18', 0);
INSERT INTO `sys_permission` VALUES (140, '后台评论列表', 'blog:comment:list', 35, '2024-01-22 09:40:40', '2024-01-22 09:40:40', 0);
INSERT INTO `sys_permission` VALUES (141, '搜索后台评论列表', 'blog:comment:search', 35, '2024-01-22 11:02:58', '2024-01-22 11:02:58', 0);
INSERT INTO `sys_permission` VALUES (142, '修改评论是否通过', 'blog:comment:isCheck', 35, '2024-01-22 20:01:50', '2024-01-22 20:01:50', 0);
INSERT INTO `sys_permission` VALUES (143, '删除评论', 'blog:comment:delete', 35, '2024-01-22 20:02:20', '2024-01-22 20:02:20', 0);
INSERT INTO `sys_permission` VALUES (144, '后台友链列表', 'blog:link:list', 39, '2024-01-22 21:03:24', '2024-01-22 21:03:24', 0);
INSERT INTO `sys_permission` VALUES (145, '搜索后台友链列表', 'blog:link:search', 39, '2024-01-22 21:03:46', '2024-01-22 21:03:46', 0);
INSERT INTO `sys_permission` VALUES (146, '修改友链是否通过', 'blog:link:isCheck', 39, '2024-01-22 21:04:18', '2024-01-22 21:04:18', 0);
INSERT INTO `sys_permission` VALUES (147, '删除友链', 'blog:link:delete', 39, '2024-01-22 21:04:46', '2024-01-22 21:04:46', 0);
INSERT INTO `sys_permission` VALUES (148, '上传站长头像', 'system:update:websiteInfo', 29, '2024-01-22 22:17:43', '2024-01-22 22:17:43', 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `role_key` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色字符',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态（0：正常，1：停用）',
  `order_num` bigint NOT NULL COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'ADMIN', 0, 0, '最高管理者', '2023-11-17 15:19:01', '2023-12-14 16:47:07', 0);
INSERT INTO `sys_role` VALUES (2, '测试角色', 'Test', 0, 1, '测试的用户，没有任何操作权限', '2023-11-17 15:19:06', '2023-12-25 16:28:33', 0);
INSERT INTO `sys_role` VALUES (3, '普通用户', 'USER', 0, 3, '前台普通用户（前台用户默认角色）', '2023-12-03 21:12:24', '2023-12-14 17:15:52', 0);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1401 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1290, 1, 21, 0);
INSERT INTO `sys_role_menu` VALUES (1292, 1, 29, 0);
INSERT INTO `sys_role_menu` VALUES (1295, 1, 31, 0);
INSERT INTO `sys_role_menu` VALUES (1296, 1, 32, 0);
INSERT INTO `sys_role_menu` VALUES (1297, 1, 33, 0);
INSERT INTO `sys_role_menu` VALUES (1298, 1, 34, 0);
INSERT INTO `sys_role_menu` VALUES (1299, 1, 43, 0);
INSERT INTO `sys_role_menu` VALUES (1300, 1, 35, 0);
INSERT INTO `sys_role_menu` VALUES (1301, 1, 37, 0);
INSERT INTO `sys_role_menu` VALUES (1302, 1, 38, 0);
INSERT INTO `sys_role_menu` VALUES (1306, 1, 1, 0);
INSERT INTO `sys_role_menu` VALUES (1307, 1, 3, 0);
INSERT INTO `sys_role_menu` VALUES (1308, 1, 64, 0);
INSERT INTO `sys_role_menu` VALUES (1309, 1, 65, 0);
INSERT INTO `sys_role_menu` VALUES (1310, 1, 2, 0);
INSERT INTO `sys_role_menu` VALUES (1311, 1, 23, 0);
INSERT INTO `sys_role_menu` VALUES (1312, 1, 24, 0);
INSERT INTO `sys_role_menu` VALUES (1313, 1, 25, 0);
INSERT INTO `sys_role_menu` VALUES (1314, 1, 26, 0);
INSERT INTO `sys_role_menu` VALUES (1315, 1, 27, 0);
INSERT INTO `sys_role_menu` VALUES (1316, 1, 44, 0);
INSERT INTO `sys_role_menu` VALUES (1345, 2, 21, 0);
INSERT INTO `sys_role_menu` VALUES (1346, 2, 29, 0);
INSERT INTO `sys_role_menu` VALUES (1349, 2, 31, 0);
INSERT INTO `sys_role_menu` VALUES (1350, 2, 32, 0);
INSERT INTO `sys_role_menu` VALUES (1351, 2, 33, 0);
INSERT INTO `sys_role_menu` VALUES (1352, 2, 34, 0);
INSERT INTO `sys_role_menu` VALUES (1353, 2, 43, 0);
INSERT INTO `sys_role_menu` VALUES (1354, 2, 35, 0);
INSERT INTO `sys_role_menu` VALUES (1355, 2, 37, 0);
INSERT INTO `sys_role_menu` VALUES (1356, 2, 38, 0);
INSERT INTO `sys_role_menu` VALUES (1360, 2, 1, 0);
INSERT INTO `sys_role_menu` VALUES (1361, 2, 3, 0);
INSERT INTO `sys_role_menu` VALUES (1362, 2, 64, 0);
INSERT INTO `sys_role_menu` VALUES (1363, 2, 2, 0);
INSERT INTO `sys_role_menu` VALUES (1364, 2, 23, 0);
INSERT INTO `sys_role_menu` VALUES (1365, 2, 24, 0);
INSERT INTO `sys_role_menu` VALUES (1366, 2, 25, 0);
INSERT INTO `sys_role_menu` VALUES (1367, 2, 26, 0);
INSERT INTO `sys_role_menu` VALUES (1368, 2, 27, 0);
INSERT INTO `sys_role_menu` VALUES (1371, 2, 44, 0);
INSERT INTO `sys_role_menu` VALUES (1380, 1, 28, 0);
INSERT INTO `sys_role_menu` VALUES (1381, 2, 28, 0);
INSERT INTO `sys_role_menu` VALUES (1382, 1, 30, 0);
INSERT INTO `sys_role_menu` VALUES (1383, 2, 30, 0);
INSERT INTO `sys_role_menu` VALUES (1384, 1, 36, 0);
INSERT INTO `sys_role_menu` VALUES (1385, 2, 36, 0);
INSERT INTO `sys_role_menu` VALUES (1386, 1, 39, 0);
INSERT INTO `sys_role_menu` VALUES (1387, 2, 39, 0);
INSERT INTO `sys_role_menu` VALUES (1396, 1, 71, 0);

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关系表id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `permission_id` bigint NOT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 271 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES (162, 1, 7);
INSERT INTO `sys_role_permission` VALUES (163, 1, 8);
INSERT INTO `sys_role_permission` VALUES (164, 1, 9);
INSERT INTO `sys_role_permission` VALUES (165, 1, 10);
INSERT INTO `sys_role_permission` VALUES (166, 1, 11);
INSERT INTO `sys_role_permission` VALUES (167, 1, 12);
INSERT INTO `sys_role_permission` VALUES (168, 1, 13);
INSERT INTO `sys_role_permission` VALUES (170, 1, 15);
INSERT INTO `sys_role_permission` VALUES (171, 1, 16);
INSERT INTO `sys_role_permission` VALUES (172, 1, 17);
INSERT INTO `sys_role_permission` VALUES (173, 1, 18);
INSERT INTO `sys_role_permission` VALUES (174, 1, 19);
INSERT INTO `sys_role_permission` VALUES (175, 1, 20);
INSERT INTO `sys_role_permission` VALUES (176, 1, 21);
INSERT INTO `sys_role_permission` VALUES (177, 1, 22);
INSERT INTO `sys_role_permission` VALUES (178, 1, 23);
INSERT INTO `sys_role_permission` VALUES (179, 1, 24);
INSERT INTO `sys_role_permission` VALUES (180, 1, 27);
INSERT INTO `sys_role_permission` VALUES (181, 1, 28);
INSERT INTO `sys_role_permission` VALUES (182, 1, 30);
INSERT INTO `sys_role_permission` VALUES (183, 1, 31);
INSERT INTO `sys_role_permission` VALUES (184, 1, 33);
INSERT INTO `sys_role_permission` VALUES (185, 1, 34);
INSERT INTO `sys_role_permission` VALUES (186, 1, 35);
INSERT INTO `sys_role_permission` VALUES (192, 1, 5);
INSERT INTO `sys_role_permission` VALUES (193, 1, 6);
INSERT INTO `sys_role_permission` VALUES (198, 1, 36);
INSERT INTO `sys_role_permission` VALUES (199, 1, 37);
INSERT INTO `sys_role_permission` VALUES (200, 1, 38);
INSERT INTO `sys_role_permission` VALUES (201, 1, 14);
INSERT INTO `sys_role_permission` VALUES (203, 2, 7);
INSERT INTO `sys_role_permission` VALUES (211, 1, 87);
INSERT INTO `sys_role_permission` VALUES (212, 1, 45);
INSERT INTO `sys_role_permission` VALUES (213, 1, 91);
INSERT INTO `sys_role_permission` VALUES (214, 1, 92);
INSERT INTO `sys_role_permission` VALUES (215, 1, 93);
INSERT INTO `sys_role_permission` VALUES (216, 1, 94);
INSERT INTO `sys_role_permission` VALUES (217, 1, 97);
INSERT INTO `sys_role_permission` VALUES (218, 1, 98);
INSERT INTO `sys_role_permission` VALUES (219, 1, 99);
INSERT INTO `sys_role_permission` VALUES (220, 1, 100);
INSERT INTO `sys_role_permission` VALUES (223, 1, 101);
INSERT INTO `sys_role_permission` VALUES (224, 1, 103);
INSERT INTO `sys_role_permission` VALUES (225, 1, 102);
INSERT INTO `sys_role_permission` VALUES (226, 1, 104);
INSERT INTO `sys_role_permission` VALUES (227, 1, 105);
INSERT INTO `sys_role_permission` VALUES (228, 1, 106);
INSERT INTO `sys_role_permission` VALUES (229, 1, 107);
INSERT INTO `sys_role_permission` VALUES (230, 1, 108);
INSERT INTO `sys_role_permission` VALUES (231, 1, 109);
INSERT INTO `sys_role_permission` VALUES (232, 1, 110);
INSERT INTO `sys_role_permission` VALUES (233, 1, 111);
INSERT INTO `sys_role_permission` VALUES (234, 1, 112);
INSERT INTO `sys_role_permission` VALUES (235, 1, 113);
INSERT INTO `sys_role_permission` VALUES (236, 1, 114);
INSERT INTO `sys_role_permission` VALUES (237, 1, 115);
INSERT INTO `sys_role_permission` VALUES (238, 1, 116);
INSERT INTO `sys_role_permission` VALUES (239, 1, 117);
INSERT INTO `sys_role_permission` VALUES (240, 1, 118);
INSERT INTO `sys_role_permission` VALUES (241, 1, 119);
INSERT INTO `sys_role_permission` VALUES (242, 1, 120);
INSERT INTO `sys_role_permission` VALUES (243, 1, 121);
INSERT INTO `sys_role_permission` VALUES (244, 1, 122);
INSERT INTO `sys_role_permission` VALUES (245, 1, 123);
INSERT INTO `sys_role_permission` VALUES (246, 1, 124);
INSERT INTO `sys_role_permission` VALUES (247, 1, 125);
INSERT INTO `sys_role_permission` VALUES (248, 1, 126);
INSERT INTO `sys_role_permission` VALUES (249, 1, 127);
INSERT INTO `sys_role_permission` VALUES (250, 1, 128);
INSERT INTO `sys_role_permission` VALUES (251, 1, 129);
INSERT INTO `sys_role_permission` VALUES (252, 1, 130);
INSERT INTO `sys_role_permission` VALUES (253, 1, 131);
INSERT INTO `sys_role_permission` VALUES (254, 1, 132);
INSERT INTO `sys_role_permission` VALUES (255, 1, 133);
INSERT INTO `sys_role_permission` VALUES (256, 1, 134);
INSERT INTO `sys_role_permission` VALUES (257, 1, 136);
INSERT INTO `sys_role_permission` VALUES (258, 1, 137);
INSERT INTO `sys_role_permission` VALUES (259, 1, 138);
INSERT INTO `sys_role_permission` VALUES (260, 1, 139);
INSERT INTO `sys_role_permission` VALUES (261, 1, 140);
INSERT INTO `sys_role_permission` VALUES (262, 1, 141);
INSERT INTO `sys_role_permission` VALUES (263, 1, 142);
INSERT INTO `sys_role_permission` VALUES (264, 1, 143);
INSERT INTO `sys_role_permission` VALUES (265, 1, 144);
INSERT INTO `sys_role_permission` VALUES (266, 1, 145);
INSERT INTO `sys_role_permission` VALUES (268, 1, 146);
INSERT INTO `sys_role_permission` VALUES (269, 1, 147);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `gender` tinyint(1) NOT NULL DEFAULT 0 COMMENT '用户性别(0,未定义,1,男,2女)',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户头像',
  `intro` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '个人简介',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `register_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '注册ip',
  `register_type` tinyint NOT NULL COMMENT '注册方式(0邮箱/姓名 1Gitee 2Github)',
  `register_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '注册地址',
  `login_ip` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近登录ip',
  `login_address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '最近登录地址',
  `login_type` tinyint NULL DEFAULT NULL COMMENT '最近登录类型(0邮箱/姓名 1Gitee 2Github)',
  `login_time` datetime NOT NULL COMMENT '用户最近登录时间',
  `is_disable` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否禁用 (0否 1是)',
  `create_time` datetime NOT NULL COMMENT '用户创建时间',
  `update_time` datetime NOT NULL COMMENT '用户更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 88065990 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'ADMIN', 'ADMIN', 0, '$2a$10$VyFtQ3T943p3NY5R0IxzIONjdqABmuCSGiHe5uV8d1ujLGYuS2KZe', 'http://minioapi.overthinker.top/cloud/logo/Logo%20%E8%AE%BE%E8%AE%A1.png', '该用户比较懒还未添加简介', 'test@qq.com', '127.0.0.1', 0, '内网IP', '127.0.0.1', '内网IP', 0, '2023-12-21 19:29:37', 0, '2023-10-13 15:16:01', '2023-12-21 19:29:37', 0);
INSERT INTO `sys_user` VALUES (88065989, 'abcd', 'abcd', 0, '$2a$10$CCqZyoDorF0gDs3GYmFMIeZewj3QselJDGpTbQZ.PP8STLAF94Kwu', 'https://image.overH.xyz/blog/user/avatar/%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.svg', '这个人很懒，什么都没有留下', 'overhyyds@outlook.com', '127.0.0.1', 0, '内网IP', NULL, NULL, NULL, '2025-02-24 03:19:48', 0, '2025-02-24 03:19:48', '2025-02-24 03:19:48', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '用户id',
  `role_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, '1');

-- ----------------------------
-- Table structure for sys_website_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_website_info`;
CREATE TABLE `sys_website_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `webmaster_avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站长头像',
  `webmaster_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站长名称',
  `webmaster_copy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站长文案',
  `webmaster_profile_background` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '站长资料卡背景图',
  `gitee_link` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'gitee链接',
  `github_link` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'github链接',
  `website_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '网站名称',
  `header_notification` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头部通知',
  `sidebar_announcement` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '侧面公告',
  `record_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备案信息',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始运行时间',
  `create_time` datetime NOT NULL COMMENT '用户创建时间',
  `update_time` datetime NOT NULL COMMENT '用户更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_website_info
-- ----------------------------
INSERT INTO `sys_website_info` VALUES (1, 'http://minioapi.overthinker.top/cloud/websiteInfo/avatar/a233c455-f1d8-4058-81e8-a8642c9fc25f.jpg', 'overthinker', '过度思考未来，无异于杀死现在的自己', 'http://minioapi.overthinker.top/cloud/websiteInfo/background/4b55d71d-45ce-4dd4-a51f-30e8cd23fadc.png', 'https://gitee.com/', 'https://github.com/', 'overthinker', '欢迎来到overthinker', '欢迎指出网站的不足，给我提供意见', '备案信息', '2024-01-01 16:00:25', '2023-12-27 14:28:10', '2025-02-24 03:11:55', 0);

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `user_id` bigint NOT NULL COMMENT '作者id',
  `category_id` bigint NOT NULL COMMENT '分类id',
  `article_cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章缩略图',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章标题',
  `article_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  `article_type` tinyint NOT NULL COMMENT '类型 (1原创 2转载 3翻译)',
  `is_top` tinyint NOT NULL COMMENT '是否置顶 (0否 1是）',
  `status` tinyint NOT NULL COMMENT '文章状态 (1公开 2私密 3草稿)',
  `visit_count` bigint NOT NULL DEFAULT 0 COMMENT '访问量',
  `create_time` datetime NOT NULL COMMENT '文章创建时间',
  `update_time` datetime NOT NULL COMMENT '文章更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_article
-- ----------------------------
INSERT INTO `t_article` VALUES (3, 22, 1, 'https://www.baidu.com', '测试文章', '测试文章内容', 1, 0, 1, 0, '2025-02-24 21:30:54', '2025-02-25 02:08:02', 0);
INSERT INTO `t_article` VALUES (41, 1, 13, 'http://minioapi.overthinker.top/cloud/article/articleCover/41455699-438b-4e36-aa3f-eec094383386.png', '你好', '# 2025 年 2 月 23 日：生活的小确幸\n\n清晨，柔和的阳光透过轻薄的窗帘，小心翼翼地洒进房间，温柔地唤醒了我。我慵懒地伸了个懒腰，新的一天就这样拉开了序幕。\n\n简单地洗漱完毕，我来到厨房，准备给自己做一顿丰盛的早餐。窗外的鸟儿欢快地唱着歌，仿佛在为这美好的一天欢呼。我煎了个金黄酥脆的鸡蛋，烤了两片香气四溢的面包，再配上一杯热气腾腾的咖啡，坐在餐桌前，细细品味着这份属于自己的宁静与满足。\n\n早餐过后，我决定出门去附近的公园散步。公园里的景色美不胜收，嫩绿的新芽在枝头探头探脑，五彩斑斓的花朵竞相绽放，散发出阵阵迷人的芬芳。我沿着蜿蜒的小径漫步，感受着微风轻拂脸颊的惬意，聆听着树叶沙沙作响的声音，仿佛与大自然融为一体。\n\n在公园里，我遇到了一些可爱的小朋友，他们在草地上嬉笑玩耍，那纯真无邪的笑容如同阳光一般灿烂。我忍不住停下脚步，看着他们尽情地享受着童年的快乐，心中也充满了温暖和喜悦。\n\n中午时分，我回到家中，简单地吃了些午餐后，便开始了自己的学习时光。我坐在书桌前，翻开一本自己喜欢的书，沉浸在知识的海洋中。时间在不知不觉中流逝，当我合上书的那一刻，仿佛经历了一场奇妙的旅行，收获满满。\n\n傍晚，我和家人一起去了一家温馨的餐厅吃晚餐。餐厅里弥漫着浓郁的美食香气，我们一边品尝着美味的菜肴，一边分享着一天中的趣事。欢声笑语回荡在餐厅里，让我感受到了家庭的温暖和幸福。\n\n晚饭后，我们沿着街道散步，欣赏着城市夜晚的美景。路灯下，人影绰绰，车辆川流不息，整个城市都散发着一种独特的魅力。我们谈论着生活中的点点滴滴，感受着这份平凡而又珍贵的时光。\n\n回到家中，我坐在窗前，望着窗外闪烁的星星，回顾着这一天的生活。虽然没有什么惊天动地的大事发生，但每一个瞬间都充满了温暖和快乐。生活或许就是这样，平淡而又真实，只要我们用心去感受，就能发现其中的小确幸。\n\n这就是 2025 年 2 月 23 日，一个平凡而又美好的日子。我期待着明天会有更多的惊喜和感动。 ', 1, 0, 1, 0, '2025-02-23 18:41:35', '2025-02-23 18:41:35', 0);
INSERT INTO `t_article` VALUES (42, 1, 13, 'http://minioapi.overthinker.top/cloud/article/articleCover/530d1a23-a1b7-4117-98b2-a8a8c4f28293.png', '一场充满惊喜与挑战的旅程', '\n生活，宛如一幅绚丽多彩的画卷，每个人都是这画卷的创作者，用自己的经历、情感和梦想，描绘出独一无二的图案。它既有阳光明媚的日子，也会有风雨交加的时刻，正是这些不同的色彩和笔触，构成了生活的丰富与深邃。\n\n## 平凡日子里的小确幸\n清晨，阳光透过窗帘的缝隙，温柔地洒在脸上，带来新一天的问候。走进厨房，煮上一杯香浓的咖啡，听着咖啡豆研磨的声音，仿佛是生活奏响的美妙序曲。伴随着咖啡的香气，翻开一本心仪已久的书籍，沉浸在文字的世界里，与作者进行一场跨越时空的对话。此时，时光仿佛慢了下来，每一个字都像是一颗璀璨的珍珠，串联起内心的宁静与满足。\n\n午后，漫步在公园的小径上，微风轻拂，树叶沙沙作响，似在诉说着大自然的故事。路边的花朵竞相绽放，红的像火，粉的像霞，白的像雪，它们在风中摇曳生姿，散发着迷人的芬芳。偶尔停下脚步，观察一只小蚂蚁忙碌的身影，看它如何搬运食物，如何与同伴交流协作，在这微小的生命中，感受到生命的顽强与坚韧。这样的午后，没有匆忙的脚步，没有繁重的工作，只有与自然的亲密接触，让人忘却一切烦恼，心中充满了对生活的热爱。\n\n夜晚，结束了一天的奔波，回到温馨的家中。与家人围坐在餐桌前，分享着一天的见闻和喜怒哀乐。餐桌上摆满了热气腾腾的饭菜，每一道菜都蕴含着家人的关爱与用心。灯光柔和地洒在每个人的脸上，欢声笑语回荡在房间里，这一刻，亲情的温暖如同一股暖流，流淌在心中，让人感受到生活的美好与幸福。\n\n## 面对挑战时的勇气与成长\n生活并非总是一帆风顺，它也会给我们带来各种各样的挑战。工作上的压力、人际关系的困扰、梦想与现实的差距，都可能让我们感到疲惫和迷茫。然而，正是这些挑战，如同磨刀石一般，磨砺着我们的意志，让我们不断成长和进步。\n\n曾经，我在工作中面临着一个巨大的项目，时间紧迫，任务艰巨，压力让我几乎喘不过气来。无数次，我在深夜里对着电脑屏幕，思考着项目的方案和细节，焦虑和不安如影随形。但是，我没有选择逃避，而是鼓起勇气，一步一个脚印地去解决问题。我主动向同事请教，查阅大量的资料，不断尝试新的方法和思路。在这个过程中，我遇到了许多困难和挫折，但每一次克服困难，都让我感受到自己的成长和进步。最终，项目顺利完成，那一刻，我心中充满了成就感，也明白了只要有勇气面对挑战，就没有什么能够阻挡我们前进的步伐。\n\n在人际关系中，我们也难免会遇到矛盾和冲突。有时候，我们会因为误解而与朋友产生隔阂，或者因为观点不同而与家人发生争执。这些经历可能会让我们感到伤心和难过，但同时也是我们学习如何理解他人、如何沟通和解决问题的宝贵机会。通过真诚地沟通和换位思考，我们往往能够化解矛盾，修复关系，让彼此的感情更加深厚。在这个过程中，我们学会了包容、学会了关爱，也更加懂得珍惜身边的人。\n\n## 追寻梦想，点亮生活的光芒\n生活中，梦想是一束照亮前行道路的光，它让我们的生活充满了希望和动力。每个人都有自己的梦想，或许是成为一名优秀的艺术家，用画笔描绘出心中的美好世界；或许是成为一名救死扶伤的医生，帮助患者战胜疾病，重获健康；或许是成为一名旅行家，踏遍世界各地，领略不同的风土人情。无论梦想是什么，它都承载着我们对生活的热爱和向往。\n\n为了实现梦想，我们需要付出努力和汗水。这意味着要在无数个日日夜夜中坚持学习，不断提升自己的能力；要在面对困难和挫折时，不轻易放弃，始终保持坚定的信念。在追寻梦想的道路上，我们会遇到各种各样的诱惑和干扰，但只要心中有梦想，我们就能够抵御这些诱惑，坚定地朝着目标前进。当我们通过自己的努力，离梦想越来越近时，那种喜悦和满足是无法用言语来形容的。梦想不仅让我们实现了自我价值，也让我们的生活变得更加充实和有意义。\n\n生活，就是这样一个充满惊喜与挑战的旅程。它既有平凡日子里的小确幸，让我们感受到生活的温暖与美好；也有面对挑战时的艰辛与成长，让我们变得更加坚强和成熟；更有追寻梦想的激情与动力，让我们的生活绽放出耀眼的光芒。在这趟旅程中，我们要用心去感受每一个瞬间，用爱去拥抱生活的每一面，因为正是这些丰富多彩的经历，构成了我们独一无二的人生。让我们怀揣着对生活的热爱，勇敢地踏上这趟奇妙的旅程，去书写属于自己的精彩篇章。 ', 1, 1, 1, 0, '2025-02-24 02:02:50', '2025-02-24 02:02:50', 0);
INSERT INTO `t_article` VALUES (43, 1, 14, 'http://minioapi.overthinker.top/cloud/article/articleCover/1ea84532-cdb1-4b84-8c05-a523d184aee6.png', '探索未知，塑造未来', '当然，我可以为你生成一篇随机的文章。以下是一篇关于“未来科技发展趋势”的示例文章：\n\n---\n\n**未来科技发展趋势：探索未知，塑造未来**\n\n在21世纪的今天，科技正以前所未有的速度改变着我们的生活。从智能手机到无人驾驶汽车，从人工智能到量子计算，科技的每一次进步都在重塑我们的世界。那么，未来的科技发展又将走向何方？本文将探讨几个关键领域，展望未来的科技发展趋势。\n\n**一、人工智能：从辅助到主导**\n\n人工智能（AI）无疑是当前科技领域的热门话题。随着深度学习、自然语言处理等技术的不断进步，AI正在逐渐从辅助工具转变为主导力量。在未来，我们可以预见AI将在更多领域发挥重要作用。例如，在医疗领域，AI将能够辅助医生进行更精准的诊断和治疗；在教育领域，AI将为学生提供个性化的学习方案，提高学习效率。此外，AI还将推动自动驾驶技术的进一步发展，使交通更加安全、高效。\n\n**二、量子计算：突破传统限制**\n\n量子计算是近年来备受瞩目的新兴技术。与传统计算机相比，量子计算机具有更高的计算速度和更强的处理能力。随着量子计算技术的不断成熟，它将在材料科学、药物研发、金融分析等领域发挥巨大作用。例如，在药物研发中，量子计算可以模拟复杂的分子结构，加速新药的发现；在金融分析中，量子计算可以处理大量数据，提高投资决策的准确性。\n\n**三、区块链：重塑信任机制**\n\n区块链技术以其去中心化、不可篡改的特点，正在逐渐改变我们对信任的理解。在未来，区块链将在金融、物流、版权保护等领域发挥重要作用。例如，在金融领域，区块链可以降低交易成本，提高交易效率；在物流领域，区块链可以确保货物的真实性和安全性；在版权保护方面，区块链可以确保创作者的知识产权得到充分保护。\n\n**四、生物技术：探索生命奥秘**\n\n生物技术是近年来发展迅速的领域之一。随着基因编辑、合成生物学等技术的不断进步，我们有望在未来实现更多关于生命的突破。例如，在医疗领域，基因编辑技术可以用于治疗遗传性疾病；在农业领域，合成生物学可以培育出更耐旱、抗病虫害的作物。此外，生物技术还将推动人类寿命的延长和生命质量的提升。\n\n**五、可持续发展技术：应对全球挑战**\n\n面对全球气候变化和资源短缺的挑战，可持续发展技术成为未来科技发展的重要方向。例如，在能源领域，可再生能源技术将逐渐取代化石燃料，成为主要的能源来源；在环保领域，废物回收和再利用技术将减少资源浪费和环境污染。此外，智能城市、绿色建筑等概念也将推动城市的可持续发展。\n\n**结语**\n\n未来的科技发展趋势充满挑战与机遇。随着人工智能、量子计算、区块链、生物技术和可持续发展技术等领域的不断进步，我们有望看到一个更加智能、高效、可持续的世界。然而，科技的发展也伴随着伦理、隐私和安全等问题。因此，在追求科技进步的同时，我们也需要关注这些问题，确保科技的发展能够真正造福人类。\n\n---\n', 1, 1, 1, 0, '2025-02-24 02:06:17', '2025-02-24 02:06:17', 0);
INSERT INTO `t_article` VALUES (44, 1, 13, 'http://minioapi.overthinker.top/cloud/article/articleCover/b8b2721c-2d45-4bc5-8d2e-81c1bd5ffca8.png', '城市绿化', '![](http://minioapi.overthinker.top/cloud/article/articleImage/72116a1f-9237-469e-bc20-81745bfdb75a.png)\n\n\n**构建生态宜居城市的绿色基石**\n\n在快速城市化的今天，高楼大厦如雨后春笋般涌现，为城市带来了前所未有的繁荣。然而，这种繁荣背后也隐藏着诸多问题，如空气污染、热岛效应、生物多样性的减少等。面对这些挑战，城市绿化作为构建生态宜居城市的关键一环，其重要性日益凸显。本文将探讨城市绿化的多重价值、面临的挑战以及实现策略。\n\n**一、城市绿化的多重价值**\n\n1. **改善空气质量**：植物通过光合作用吸收二氧化碳，释放氧气，有效缓解城市空气污染问题。同时，绿植还能吸附空气中的尘埃和有害物质，提升空气质量。\n\n2. **调节城市微气候**：城市绿地如同城市的“天然空调”，通过蒸腾作用降低周围温度，缓解热岛效应。此外，绿地还能增加空气湿度，改善城市小气候。\n\n3. **提升居民生活质量**：绿色空间为城市居民提供了休闲娱乐的好去处，有助于缓解生活压力，提升心理健康。同时，绿地的存在也增强了城市的审美价值，提升了居民的生活满意度。\n\n4. **保护生物多样性**：城市绿地是城市生物多样性的重要栖息地，为鸟类、昆虫等野生动物提供了生存空间，有助于维护生态平衡。\n\n**二、城市绿化面临的挑战**\n\n尽管城市绿化的价值显而易见，但在实施过程中仍面临诸多挑战。首先，城市用地紧张，绿地规划往往受到空间限制。其次，绿化维护成本较高，需要持续的资金投入。此外，公众对绿化的认识和参与度也是影响绿化效果的关键因素。\n\n**三、城市绿化的实现策略**\n\n1. **科学规划，合理布局**：在城市规划中，应将绿地作为城市基础设施的重要组成部分，科学规划，合理布局。通过建设城市公园、街头绿地、屋顶花园等多种形式，增加城市绿地面积。\n\n2. **鼓励公众参与，提升绿化意识**：通过宣传教育、社区活动等方式，提高公众对城市绿化的认识，鼓励居民参与绿化种植、养护等活动，形成全民共建共享的良好氛围。\n\n3. **引入市场机制，创新绿化模式**：探索政府引导、市场运作的绿化模式，吸引社会资本参与城市绿化建设。如通过PPP（政府和社会资本合作）模式，引入专业绿化企业进行绿化项目的投资、建设和运营。\n\n4. **加强科技支撑，提升绿化效率**：运用现代科技手段，如智能灌溉、远程监控等，提高绿化养护的精准度和效率。同时，研发适应城市环境的优良绿化树种，丰富城市绿化品种。\n\n5. **注重生态修复，保护自然遗产**：在城市绿化过程中，应注重对受损生态系统的修复，如利用城市废弃地建设生态公园，既美化城市环境，又保护自然遗产。\n\n**结语**\n\n城市绿化是构建生态宜居城市的重要基石，对于提升城市品质、改善居民生活质量具有不可替代的作用。面对城市化进程中的诸多挑战，我们应积极探索科学的绿化策略，鼓励公众参与，加强科技支撑，共同打造绿色、健康、和谐的城市生态环境。\n\n---\n\n请注意，以上文章为随机生成，仅供参考，具体数据和情况可能因地区和时间的不同而有所变化。', 1, 1, 1, 0, '2025-02-24 02:25:46', '2025-02-24 02:25:46', 0);
INSERT INTO `t_article` VALUES (45, 1, 14, 'http://minioapi.overthinker.top/cloud/article/articleCover/3655f106-2452-4403-91bc-ac65b3cfe8c7.png', '未来工作与职业发展的新趋势', '\n\n**：迎接变革，塑造未来**\n\n在21世纪的今天，我们正经历着前所未有的职业与工作方式的变革。随着科技的飞速发展、全球化的深入以及社会需求的不断变化，未来的工作与职业发展将呈现出一系列新的趋势。本文将探讨这些趋势，并思考它们如何影响我们的职业道路和工作方式。\n\n**一、远程工作与灵活工作制的兴起**\n\n随着互联网的普及和技术的进步，远程工作已成为越来越多人的选择。特别是在疫情期间，远程工作更是成为了许多行业的标配。未来，随着云计算、大数据、人工智能等技术的进一步发展，远程工作和灵活工作制将更加普及。这种工作方式不仅提高了工作效率，还为员工提供了更多的自由和灵活性，有助于提升工作满意度和生活质量。\n\n**二、技能导向的职业发展**\n\n在未来的职场中，技能将成为衡量个人价值的重要标准。随着技术的不断进步和行业的快速变化，许多传统职业正在被自动化取代，而新兴职业则不断涌现。因此，持续学习和掌握新技能将成为职场人士必备的能力。未来，职业发展将更加注重个人的技能提升和跨界能力，而不仅仅是学历和工作经验。\n\n**三、项目制与团队合作的加强**\n\n随着企业组织结构的扁平化和项目管理的普及，项目制和团队合作将成为未来职场的主流模式。在这种模式下，员工将更多地以项目为单位进行工作，与来自不同背景和专业的团队成员紧密合作。这种工作方式不仅提高了工作效率和创新能力，还促进了员工之间的交流和协作，有助于构建更加和谐、包容的职场文化。\n\n**四、创业与自主就业的兴起**\n\n随着创业环境的改善和自主就业政策的支持，越来越多的人选择创业或自主就业作为自己的职业道路。未来，随着数字化、智能化等技术的进一步发展，创业和自主就业的机会将更加丰富多样。这种职业选择不仅有助于个人实现自我价值，还能为社会创造更多的就业机会和经济增长点。\n\n**五、关注员工福利与心理健康**\n\n在未来的职场中，员工福利和心理健康将受到更多的关注。随着工作压力的增大和生活节奏的加快，员工的身心健康问题日益凸显。因此，企业将更加重视员工福利和心理健康的保障，通过提供健康保险、心理咨询等服务，帮助员工缓解压力、提升幸福感。\n\n**结语**\n\n未来工作与职业发展的新趋势将深刻影响我们的职业道路和工作方式。面对这些变革，我们应保持开放的心态和积极的态度，不断学习和提升自己的技能和能力。同时，我们也应关注自己的身心健康和职业发展需求，努力在变革中找到属于自己的职业道路和工作方式。只有这样，我们才能在未来的职场中立于不败之地，迎接更加美好的未来。\n\n---\n\n', 1, 0, 1, 0, '2025-02-24 02:28:18', '2025-02-24 02:28:18', 0);
INSERT INTO `t_article` VALUES (46, 1, 13, 'http://minioapi.overthinker.top/cloud/article/articleCover/e2d1ec3d-5e18-4e42-9fa4-cce9c4d2a1f5.png', '探索未来教育的创新之路', '\n\n**科技与人文的融合**\n\n在人类历史的长河中，教育始终是推动社会进步和文明发展的重要力量。随着科技的飞速发展和社会需求的不断变化，未来教育正面临着前所未有的挑战与机遇。本文旨在探讨未来教育的创新之路，特别是科技与人文如何在这一进程中实现深度融合，共同塑造更加公平、高效、个性化的教育体系。\n\n**一、科技驱动的教育变革**\n\n近年来，人工智能、大数据、云计算等先进技术的快速发展，为教育领域带来了深刻的变革。智能教学系统能够根据学生的学习进度和兴趣，提供个性化的学习路径和资源，从而提高学习效率。同时，虚拟现实（VR）、增强现实（AR）等技术的应用，更是为学生创造了沉浸式的学习环境，使抽象知识变得直观易懂。此外，在线学习平台的兴起，打破了地域限制，让优质教育资源得以广泛传播，促进了教育公平。\n\n**二、人文精神的回归与融合**\n\n然而，科技的快速发展也带来了教育领域的某些“异化”现象。过度依赖技术可能导致学生缺乏批判性思维、创新能力和人际交往能力。因此，未来教育在追求技术革新的同时，必须回归人文精神，强调学生的全面发展。这包括培养学生的价值观、社会责任感、审美情趣以及跨文化交流能力。通过人文学科的融入，使教育不仅仅是知识的传授，更是品格的塑造和精神的滋养。\n\n**三、创新教育模式：科技与人文的融合实践**\n\n1. **项目式学习（PBL）**：结合科技手段，如在线协作平台、数据分析工具等，开展跨学科的项目式学习。学生围绕真实世界的问题进行探究，不仅锻炼了问题解决能力，还促进了团队合作和批判性思维的发展。\n\n2. **翻转课堂**：利用视频、在线课程等数字化资源，让学生在课外自主学习基础知识，课堂时间则用于深度讨论、实践操作和个性化指导。这种模式既发挥了科技的优势，又强调了师生互动和人文关怀。\n\n3. **社区参与与社会实践**：鼓励学生参与社区服务、环保项目等社会实践活动，通过亲身体验，增进对社会的理解，培养公民意识和社会责任感。同时，利用科技手段记录、分析和反思这些经历，深化学习效果。\n\n**四、面向未来的教育挑战与应对策略**\n\n尽管科技与人文的融合为教育创新提供了无限可能，但仍面临诸多挑战。如教育资源的分配不均、教师角色的转变与培训、学生自主学习能力的培养等。为此，政府、学校、企业和社会各界需共同努力，制定更加公平、可持续的教育政策，加强教师队伍建设，推动教育技术创新与应用，同时注重学生的人文素养培养。\n\n**结语**\n\n未来教育的创新之路，是一条科技与人文深度融合的探索之旅。在这条路上，我们既要充分利用科技的力量，提升教育的效率和质量，也要坚守人文的底线，关注学生的全面发展。只有这样，我们才能培养出既有扎实知识，又具备创新精神和社会责任感的未来公民，共同迎接更加美好的明天。\n\n---\n', 1, 0, 1, 0, '2025-02-24 02:29:38', '2025-02-24 02:29:38', 0);
INSERT INTO `t_article` VALUES (47, 1, 14, 'http://minioapi.overthinker.top/cloud/article/articleCover/8b4eb727-09b8-457d-a710-a1f672a29316.png', '未来城市', '\n\n**智能与绿色的交响曲**\n\n在21世纪的曙光中，我们正见证着城市面貌的深刻变革。随着科技的飞速进步和全球环保意识的增强，未来城市正逐步向我们展现出一幅智能与绿色交织的宏伟蓝图。这是一场前所未有的城市革命，它不仅仅是建筑和技术的革新，更是人类生活方式和思维方式的深刻转变。\n\n**一、智能城市的崛起**\n\n智能城市，作为未来城市的核心形态，正在全球范围内迅速崛起。它通过物联网、大数据、云计算、人工智能等先进技术，将城市的基础设施、公共服务、交通出行、环境保护等各个方面进行智能化改造和升级。智能城市不仅提高了城市的运行效率和居民的生活质量，还带来了前所未有的便捷性和安全性。\n\n在智能城市中，交通出行将变得更加智能和高效。自动驾驶汽车、智能交通管理系统、智能停车系统等技术的应用，将大大缓解城市交通拥堵问题，提高出行效率。同时，智能城市还能通过大数据分析，预测和应对各种城市问题，如公共安全、环境污染等，从而确保城市的稳定和安全。\n\n**二、绿色城市的呼唤**\n\n与智能城市并行发展的，是绿色城市的理念和实践。在全球气候变化和环境污染日益严重的背景下，绿色城市成为了未来城市发展的必然趋势。绿色城市强调可持续发展，注重节能减排、资源循环利用和生态环境保护。\n\n在绿色城市中，绿色建筑将成为主流。这些建筑通过采用先进的节能技术和材料，大幅降低能耗和碳排放。同时，绿色城市还将大力发展可再生能源，如太阳能、风能等，以减少对化石燃料的依赖。此外，绿色城市还注重城市绿化和生态保护，通过建设城市公园、湿地保护区等，为城市居民提供优美的生态环境和休闲空间。\n\n**三、智能与绿色的交响曲**\n\n未来城市的发展，将是智能与绿色交织的交响曲。智能城市的技术优势，将为绿色城市的发展提供有力支持。例如，智能城市中的大数据分析技术，可以帮助城市管理者更好地了解城市能源使用情况，从而制定更加科学的节能减排政策。同时，智能城市中的物联网技术，可以实时监测和控制城市中的各种设备，实现能源的高效利用和资源的循环利用。\n\n而绿色城市的发展理念，也将为智能城市的建设提供重要指导。绿色城市强调可持续发展和生态平衡，这将促使智能城市在发展过程中，更加注重环境保护和生态平衡。同时，绿色城市中的绿色建筑和可再生能源技术，也将为智能城市提供更加环保和可持续的能源解决方案。\n\n**四、未来城市的展望**\n\n未来城市的发展，将是一场智能与绿色的交响曲。在这场交响曲中，我们将看到科技与自然、人类与环境的和谐共生。未来城市将成为一个充满智慧、活力和生机的美好家园，为人类社会带来前所未有的繁荣和进步。\n\n然而，未来城市的发展也面临着诸多挑战。如何平衡智能与绿色的发展关系？如何确保城市发展的可持续性和生态平衡？这些问题需要我们不断探索和实践，以找到最佳的解决方案。\n\n总之，未来城市的发展将是一场充满挑战和机遇的旅程。我们相信，在科技与自然、人类与环境的共同努力下，未来城市一定能够成为一个更加美好、更加繁荣的家园。\n\n---\n', 1, 0, 1, 0, '2025-02-24 02:44:16', '2025-02-24 02:44:16', 0);

-- ----------------------------
-- Table structure for t_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_article_tag`;
CREATE TABLE `t_article_tag`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '关系表id',
  `article_id` bigint UNSIGNED NOT NULL COMMENT '文章id',
  `tag_id` bigint NOT NULL COMMENT '标签id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 96 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_article_tag
-- ----------------------------
INSERT INTO `t_article_tag` VALUES (89, 41, 14, '2025-02-23 18:41:35', 0);
INSERT INTO `t_article_tag` VALUES (90, 42, 14, '2025-02-24 02:02:50', 0);
INSERT INTO `t_article_tag` VALUES (91, 43, 15, '2025-02-24 02:06:17', 0);
INSERT INTO `t_article_tag` VALUES (92, 44, 16, '2025-02-24 02:25:46', 0);
INSERT INTO `t_article_tag` VALUES (93, 45, 16, '2025-02-24 02:28:18', 0);
INSERT INTO `t_article_tag` VALUES (94, 46, 16, '2025-02-24 02:29:38', 0);
INSERT INTO `t_article_tag` VALUES (95, 47, 16, '2025-02-24 02:44:16', 0);

-- ----------------------------
-- Table structure for t_banners
-- ----------------------------
DROP TABLE IF EXISTS `t_banners`;
CREATE TABLE `t_banners`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片路径',
  `size` bigint NOT NULL COMMENT '图片大小 (字节)',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '图片类型 (MIME)',
  `user_id` bigint NOT NULL COMMENT '上传人id',
  `sort_order` int NOT NULL COMMENT '图片顺序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_banners
-- ----------------------------
INSERT INTO `t_banners` VALUES (40, 'http://minioapi.overthinker.top/cloud/banners/78e95b4f-5344-4369-8493-da855084b59f.png', 3713281, 'image/png', 1, 2, '2025-02-24 02:59:32');
INSERT INTO `t_banners` VALUES (41, 'http://minioapi.overthinker.top/cloud/banners/853468cf-3192-40a0-95a1-f322dba4e094.png', 4203560, 'image/png', 1, 3, '2025-02-24 03:12:08');

-- ----------------------------
-- Table structure for t_category
-- ----------------------------
DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `category_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '分类名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO `t_category` VALUES (13, '生活', '2025-02-23 18:38:23', '2025-02-23 18:38:23', 0);
INSERT INTO `t_category` VALUES (14, '科技', '2025-02-24 02:05:49', '2025-02-24 02:05:49', 0);

-- ----------------------------
-- Table structure for t_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `type` tinyint(1) NOT NULL COMMENT '评论类型 (1文章 2留言板)',
  `type_id` bigint NOT NULL COMMENT '类型id',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父评论id',
  `reply_id` bigint NULL DEFAULT NULL COMMENT '回复评论id',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论的内容',
  `comment_user_id` bigint NOT NULL COMMENT '评论用户的id',
  `reply_user_id` bigint NULL DEFAULT NULL COMMENT '回复用户的id',
  `is_check` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否通过 (0否 1是)',
  `create_time` datetime NOT NULL COMMENT '评论时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_comment
-- ----------------------------
INSERT INTO `t_comment` VALUES (43, 2, 4, NULL, NULL, '666[哭泣]', 1, NULL, 1, '2023-11-06 11:30:23', '2023-11-06 11:30:23', 0);
INSERT INTO `t_comment` VALUES (44, 2, 4, 43, 43, '哈哈哈🤑', 1, 1, 1, '2023-11-06 11:32:30', '2023-11-06 11:32:30', 0);
INSERT INTO `t_comment` VALUES (47, 2, 4, 41, 46, '好像是', 1, 1, 1, '2023-11-06 11:35:34', '2023-11-06 11:35:34', 0);
INSERT INTO `t_comment` VALUES (48, 2, 3, NULL, NULL, '你好啊[扶额]', 1, NULL, 1, '2023-12-17 17:13:09', '2023-12-17 17:13:09', 0);
INSERT INTO `t_comment` VALUES (51, 2, 2, NULL, NULL, '😦', 1, NULL, 1, '2024-01-07 21:24:30', '2024-01-07 21:24:30', 0);

-- ----------------------------
-- Table structure for t_favorite
-- ----------------------------
DROP TABLE IF EXISTS `t_favorite`;
CREATE TABLE `t_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏id',
  `user_id` bigint NOT NULL COMMENT '收藏的用户id',
  `type` tinyint NOT NULL COMMENT '收藏类型(1,文章 2,留言板)',
  `type_id` bigint NOT NULL COMMENT '类型id',
  `is_check` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否有效 (0否 1是)',
  `create_time` datetime NOT NULL COMMENT '收藏时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 169 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_favorite
-- ----------------------------

-- ----------------------------
-- Table structure for t_leave_word
-- ----------------------------
DROP TABLE IF EXISTS `t_leave_word`;
CREATE TABLE `t_leave_word`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT '留言用户id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '留言内容',
  `is_check` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否通过 (0否 1是)',
  `create_time` datetime NOT NULL COMMENT '留言时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_leave_word
-- ----------------------------
INSERT INTO `t_leave_word` VALUES (13, 1, '## 测试比较长的留言\\n\\n> 下面是一篇 c# 笔记\\n\\n<center>\\n    <h1>C#笔记</h1>\\n</center>\\n\\n\\n\\n[TOC] \\n\\n### 1、C#访问修饰符\\n\\n​\\t\\t在C#当中的访问修饰符及作用范围如下：\\n\\n|     访问修饰符     |                        说明                        |\\n| :----------------: | :------------------------------------------------: |\\n|       public       |              共有访问。不受任何限制。              |\\n|      private       |      私有访问。只有本类能访问，实例不能访问。      |\\n|     protected      |   保护访问。只限于本类和子类访问，实例不能访问。   |\\n|      internal      |      内部访问。只限于本项目访问，其他不能访问      |\\n| protected internal | 内部保护访问。只限于本项目或子类访问，其他不能访问 |\\n\\n​\\t\\tC#成员类型的可修饰及默认修饰符如下：\\n\\n| 成员类型  | 默认修饰符 |                           可被修饰                           |\\n| :-------: | :--------: | :----------------------------------------------------------: |\\n|   enum    |   public   |                             none                             |\\n|   class   |  private   | public、protected、internal、private、<br />protected internal |\\n| interface |   public   |                             none                             |\\n|  struct   |  private   |                  public、internal、private                   |\\n\\n> public 访问级别最高\\n>\\n> private 访问级别最低\\n\\n### 2、this 关键字\\n\\n看以下代码，有什么问题：\\n\\n```C#\\nclass Strdent\\n{\\n    private string _name;\\t//姓名\\t\\n    public int _age = 19;\\t//年龄\\n    public string _cardID = \\\"145236985674526685\\\";\\t//身份证号码\\n    public void SetName(string _name)\\n    {\\n        _name = _name;\\n    }\\n}\\n```\\n\\n分析： 在 Student 类中定义了一个 private 成员变量 _name,在 SetName()方法的参数中也定义了一个与之同名的变量 _name。这时编译器会发现成员变量和方法的参数重名了。\\n此时，编译器无法分辨代码中出现的这两个 _name 那个是成员变量，哪个是方法中的参数。我们可以借助 this 关键字来解决这个问题。\\n\\n> this 关键字是指当前对象本身。通过 this 可以引用当前类的成员变量和方法。\\n\\n因此可以改变以上代码为：\\n\\n```C#\\nclass Strdent\\n{\\n    private string _name;\\t//姓名\\t\\n    public int _age = 19;\\t//年龄\\n    public string _cardID = \\\"145236985674526685\\\";\\t//身份证号码\\n    public void SetName(string _name)\\n    {\\n        this._name = _name;\\n    }\\n}\\n```\\n\\n> 使用 this 关键字可以解决成员变量和局部变量名称冲突的问题。\\n\\n### 3、C#属性\\n\\n#### 3.1、用方法保证数据安全', 1, '2024-01-16 12:15:27', '2024-01-16 12:15:27', 0);
INSERT INTO `t_leave_word` VALUES (23, 1, '# \\u6DFB\\u52A0\\u7559\\u8A00\\u677F\\\\n\\\\n* \\u6DFB\\u52A0\\u6D4B\\u8BD5\\\\n* dddd', 1, '2024-01-16 13:10:40', '2024-01-16 13:10:40', 0);
INSERT INTO `t_leave_word` VALUES (24, 1, '# 添加留言板\n\n* 添加测试aaa', 1, '2024-01-16 13:16:24', '2024-01-16 13:16:24', 0);
INSERT INTO `t_leave_word` VALUES (25, 1, '## 测试比较长的留言\n\n> 下面是一篇 c# 笔记\n\n<center>\n    <h1>C#笔记</h1>\n</center>\n\n\n\n[TOC] \n\n### 1、C#访问修饰符\n\n​		在C#当中的访问修饰符及作用范围如下：\n\n|     访问修饰符     |                        说明                        |\n| :----------------: | :------------------------------------------------: |\n|       public       |              共有访问。不受任何限制。              |\n|      private       |      私有访问。只有本类能访问，实例不能访问。      |\n|     protected      |   保护访问。只限于本类和子类访问，实例不能访问。   |\n|      internal      |      内部访问。只限于本项目访问，其他不能访问      |\n| protected internal | 内部保护访问。只限于本项目或子类访问，其他不能访问 |\n\n​		C#成员类型的可修饰及默认修饰符如下：\n\n| 成员类型  | 默认修饰符 |                           可被修饰                           |\n| :-------: | :--------: | :----------------------------------------------------------: |\n|   enum    |   public   |                             none                             |\n|   class   |  private   | public、protected、internal、private、<br />protected internal |\n| interface |   public   |                             none                             |\n|  struct   |  private   |                  public、internal、private                   |\n\n> public 访问级别最高\n>\n> private 访问级别最低\n\n### 2、this 关键字\n\n看以下代码，有什么问题：\n\n```C#\nclass Strdent\n{\n    private string _name;	//姓名	\n    public int _age = 19;	//年龄\n    public string _cardID = \"145236985674526685\";	//身份证号码\n    public void SetName(string _name)\n    {\n        _name = _name;\n    }\n}\n```\n\n分析： 在 Student 类中定义了一个 private 成员变量 _name,在 SetName()方法的参数中也定义了一个与之同名的变量 _name。这时编译器会发现成员变量和方法的参数重名了。\n此时，编译器无法分辨代码中出现的这两个 _', 1, '2024-01-16 13:25:08', '2024-01-16 13:25:08', 0);
INSERT INTO `t_leave_word` VALUES (26, 1, '## 测试留言Markdown 编写\n\n> 不要报错\n\n> ~~没有bug~~', 1, '2024-01-16 13:27:50', '2024-01-16 13:27:50', 0);
INSERT INTO `t_leave_word` VALUES (27, 1, '### 留言bug 最后测试', 1, '2024-01-16 13:29:34', '2024-01-16 13:30:23', 0);

-- ----------------------------
-- Table structure for t_like
-- ----------------------------
DROP TABLE IF EXISTS `t_like`;
CREATE TABLE `t_like`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞表id',
  `user_id` bigint NOT NULL COMMENT '点赞的用户id',
  `type` tinyint NOT NULL COMMENT '点赞类型(1,文章,2,评论,3留言板)',
  `type_id` bigint NOT NULL COMMENT '点赞的文章id',
  `create_time` datetime NOT NULL COMMENT '点赞时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 299 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_like
-- ----------------------------
INSERT INTO `t_like` VALUES (257, 1, 2, 5, '2023-11-06 11:02:40', '2023-11-06 11:02:40');
INSERT INTO `t_like` VALUES (261, 1, 3, 4, '2023-11-06 11:28:30', '2023-11-06 11:28:30');
INSERT INTO `t_like` VALUES (262, 1, 3, 5, '2023-11-06 11:28:36', '2023-11-06 11:28:36');
INSERT INTO `t_like` VALUES (263, 1, 2, 46, '2023-11-06 11:35:39', '2023-11-06 11:35:39');
INSERT INTO `t_like` VALUES (264, 1, 2, 41, '2023-11-06 11:35:41', '2023-11-06 11:35:41');
INSERT INTO `t_like` VALUES (269, 1, 2, 26, '2023-12-11 16:44:51', '2023-12-11 16:44:51');

-- ----------------------------
-- Table structure for t_link
-- ----------------------------
DROP TABLE IF EXISTS `t_link`;
CREATE TABLE `t_link`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '友链表id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站名称',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站地址',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站描述',
  `background` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '网站背景',
  `is_check` tinyint NOT NULL DEFAULT 0 COMMENT '审核状态（0：未通过，1：已通过）',
  `email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_link
-- ----------------------------
INSERT INTO `t_link` VALUES (18, 1, '无语小站', '  http://localhost:99/', '无语小站无语小站', 'http://cdn.kuailemao.lielfw.cn/articleCover/21676717033297579.jpg', 1, '3490223402@qq.com', '2024-01-22 21:55:08', '2024-01-22 21:55:36', 0);

-- ----------------------------
-- Table structure for t_photo
-- ----------------------------
DROP TABLE IF EXISTS `t_photo`;
CREATE TABLE `t_photo`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint NOT NULL COMMENT '创建者id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '名称',
  `description` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `type` tinyint(1) NOT NULL COMMENT '类型（1：相册 2：照片）',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父相册id',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片地址',
  `is_check` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否通过 (0否 1是)',
  `size` double NULL DEFAULT NULL COMMENT '照片体积大小(kb)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 154 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_photo
-- ----------------------------
INSERT INTO `t_photo` VALUES (152, 1, 'minio', 'test', 1, NULL, NULL, 1, NULL, '2025-02-23 14:41:59', '2025-02-23 14:42:03', 0);
INSERT INTO `t_photo` VALUES (153, 1, 'OIP-C', NULL, 2, 152, 'http://minioapi.overthinker.top/cloud/photoAlbum/minio/OIP-C.jpg', 1, 0.02, '2025-02-23 14:42:10', '2025-02-23 14:42:10', 0);

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `tag_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `create_time` datetime NOT NULL COMMENT '标签创建时间',
  `update_time` datetime NOT NULL COMMENT '标签更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_tag
-- ----------------------------
INSERT INTO `t_tag` VALUES (14, '测试标签', '2025-02-23 18:38:13', '2025-02-23 18:38:13', 0);
INSERT INTO `t_tag` VALUES (15, '人工智能', '2025-02-24 02:06:04', '2025-02-24 02:06:04', 0);
INSERT INTO `t_tag` VALUES (16, '社会', '2025-02-24 02:25:30', '2025-02-24 02:25:30', 0);

-- ----------------------------
-- Table structure for t_tree_hole
-- ----------------------------
DROP TABLE IF EXISTS `t_tree_hole`;
CREATE TABLE `t_tree_hole`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '树洞表id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '内容',
  `is_check` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否通过 (0否 1是)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_tree_hole
-- ----------------------------
INSERT INTO `t_tree_hole` VALUES (1, 1, '测试添加', 1, '2023-10-30 11:32:30', '2023-10-30 11:32:30', 0);
INSERT INTO `t_tree_hole` VALUES (29, 1, '真的是服了！！', 1, '2023-10-30 16:41:15', '2023-10-30 16:41:15', 0);
INSERT INTO `t_tree_hole` VALUES (30, 1, '记得一定要快乐啊！！', 1, '2023-10-30 16:41:57', '2024-01-19 21:31:21', 0);
INSERT INTO `t_tree_hole` VALUES (34, 1, '天天开心', 1, '2024-01-19 21:33:24', '2024-01-19 21:33:24', 0);

-- ----------------------------
-- Table structure for t_video
-- ----------------------------
DROP TABLE IF EXISTS `t_video`;
CREATE TABLE `t_video`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `category_id` bigint NOT NULL COMMENT '分类',
  `vedio_cover` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '视频封面',
  `vedio` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '视频地址',
  `vedio_title` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '视频标题',
  `vedio_description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '视频介绍',
  `vedio_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '视频格式',
  `permission` tinyint(1) NOT NULL COMMENT '视频权限1用户专属，2公开',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态1 不知道干什么',
  `vedio_size` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '大小（单位自动转换）',
  `vedio_count` bigint NOT NULL DEFAULT 0 COMMENT '视频访问量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除默认1 没有删除 ，2 已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_video
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
