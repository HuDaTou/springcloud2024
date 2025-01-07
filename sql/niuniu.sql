/*
 Navicat Premium Data Transfer

 Source Server         : homeubuntu22
 Source Server Type    : MySQL
 Source Server Version : 80401 (8.4.1)
 Source Host           : home.overthinker.top:30970
 Source Schema         : niuniu

 Target Server Type    : MySQL
 Target Server Version : 80401 (8.4.1)
 File Encoding         : 65001

 Date: 26/08/2024 15:16:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = InnoDB AUTO_INCREMENT = 2868 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_log
-- ----------------------------
INSERT INTO `sys_log` VALUES (2867, '文章管理', '获取', 'ADMIN', 'fd02::dc4:c5c9:fc6b:e80', '内网IP', 0, 'com.overthinker.cloud.web.controller.ArticleController.listArticle()', '[]', 'GET', NULL, '{\"code\":200,\"data\":[{\"articleCover\":\"http://43.136.78.47:9000/blog/article/articleCover/cfce7f30-f9e7-4ca4-81b9-c0831cc9c221.png\",\"articleTitle\":\"测试\",\"articleType\":1,\"categoryId\":1,\"categoryName\":\"生活\",\"createTime\":1709129136000,\"id\":40,\"isTop\":0,\"status\":1,\"tagsName\":[\"python\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":0},{\"articleCover\":\"http://43.136.78.47:9000/blog/article/articleCover/d4574635-ba1d-4c01-beda-1ad25c4db0e3.png\",\"articleTitle\":\"测试文章\",\"articleType\":1,\"categoryId\":4,\"categoryName\":\"分类\",\"createTime\":1704403939000,\"id\":32,\"isTop\":0,\"status\":1,\"tagsName\":[\"python\",\"c#\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":7},{\"articleCover\":\"http://cdn.kuailemao.lielfw.cn/articleCover/Sara11676693097117968.png\",\"articleTitle\":\"java的继承\",\"articleType\":1,\"categoryId\":2,\"categoryName\":\"技术\",\"createTime\":1697336805000,\"id\":1,\"isTop\":1,\"status\":1,\"tagsName\":[\"java\",\"python\",\"c#\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":266},{\"articleCover\":\"http://cdn.kuailemao.lielfw.cn/PicGo/idea%E8%83%8C%E6%99%AF.png\",\"articleTitle\":\"今天出去玩了\",\"articleType\":1,\"categoryId\":2,\"categoryName\":\"技术\",\"createTime\":1697336805000,\"id\":3,\"isTop\":1,\"status\":1,\"tagsName\":[\"c#\",\"开心\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":133},{\"articleCover\":\"http://cdn.kuailemao.lielfw.cn/PicGo/idea%E8%83%8C%E6%99%AF.png\",\"articleTitle\":\"测试分类3\",\"articleType\":1,\"categoryId\":3,\"categoryName\":\"娱乐\",\"createTime\":1697336805000,\"id\":24,\"isTop\":0,\"status\":1,\"tagsName\":[\"python\",\"c++\"],\"userId\":1,\"userName\":\"ADMIN\",\"visitCount\":131}],\"msg\":\"success\"}', '/article/back/list', 355, '获取所有的文章列表', '2024-08-21 10:05:39', '2024-08-21 10:05:39', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 466 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------
INSERT INTO `sys_login_log` VALUES (464, 'admin', 'fd02::dc4:c5c9:fc6b:e80', '内网IP', 'Chrome 12-127.0.0.0', 'Windows 10', 1, 0, '登录成功', '2024-08-21 10:02:42', '2024-08-21 10:02:42', 0);
INSERT INTO `sys_login_log` VALUES (465, 'overthinker', 'fd02::dc4:c5c9:fc6b:e80', '内网IP', 'Chrome 12-127.0.0.0', 'Windows 10', 0, 1, '用户名或密码错误', '2024-08-21 10:04:08', '2024-08-21 10:04:08', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 71 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

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
INSERT INTO `sys_menu` VALUES (42, '数据大屏', 'PieChartTwoTone', '/data-screen', '/data-screen', NULL, 0, NULL, NULL, 1, NULL, 1, 1, 1, '', 0, 4, '2023-11-29 20:51:14', '2024-01-22 22:07:04', 0);
INSERT INTO `sys_menu` VALUES (43, '收藏管理', 'InboxOutlined', '/blog/collect', '/blog/collect', NULL, 0, 28, NULL, 0, NULL, 1, 1, 1, '', 0, 3, '2023-11-29 20:54:15', '2023-11-29 20:54:47', 0);
INSERT INTO `sys_menu` VALUES (44, '服务监控', 'AlertOutlined', '/system/server-monitoring', '/system/server-monitoring', NULL, 0, 1, NULL, 0, NULL, 1, 1, 1, '', 0, 6, '2023-11-29 21:01:24', '2023-12-14 15:26:34', 0);
INSERT INTO `sys_menu` VALUES (64, '角色授权', '', '/role/authorization', '/system/role/user-role', NULL, 0, 1, NULL, 1, NULL, 1, 1, 1, '', 0, 0, '2023-12-04 12:07:00', '2023-12-05 09:57:09', 0);
INSERT INTO `sys_menu` VALUES (65, '权限授权', '', '/permission/authorization', '/system/permission/role-permission', NULL, 0, 1, NULL, 1, NULL, 1, 1, 1, '', 0, 0, '2023-12-07 14:38:45', '2023-12-07 14:41:44', 0);
INSERT INTO `sys_menu` VALUES (68, '用户授权', '', '/user/role', '/system/user/role-user', NULL, 0, 1, NULL, 1, NULL, 1, 1, 1, '', 0, 0, '2023-12-19 10:37:05', '2023-12-19 10:38:16', 0);
INSERT INTO `sys_menu` VALUES (69, '接口文档', 'FileTextTwoTone', 'http://blog.kuailemao.xyz/doc.html#/home', NULL, NULL, 0, NULL, NULL, 0, 'http://127.0.0.1:8088/doc.html#/home', 1, 1, 1, '_blank', 0, 5, '2024-01-22 20:32:18', '2024-02-28 14:03:19', 0);
INSERT INTO `sys_menu` VALUES (70, '跳转前台', 'TabletTwoTone', 'http://blog.kuailemao.xyz', NULL, NULL, 0, NULL, NULL, 0, '', 1, 1, 1, '_blank', 0, 6, '2024-01-22 20:38:54', '2024-02-28 14:02:45', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 1395 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

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
INSERT INTO `sys_role_menu` VALUES (1392, 1, 42, 0);
INSERT INTO `sys_role_menu` VALUES (1393, 2, 42, 0);
INSERT INTO `sys_role_menu` VALUES (1394, 1, 69, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 88065989 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'ADMIN', 'ADMIN', 0, '$2a$10$VyFtQ3T943p3NY5R0IxzIONjdqABmuCSGiHe5uV8d1ujLGYuS2KZe', 'http://cdn.kuailemao.lielfw.cn/articleCover/Sara11676693014447852.png', '该用户比较懒还未添加简介', 'test@qq.com', '127.0.0.1', 0, '内网IP', '127.0.0.1', '内网IP', 0, '2023-12-21 19:29:37', 0, '2023-10-13 15:16:01', '2023-12-21 19:29:37', 0);
INSERT INTO `sys_user` VALUES (2, '快乐猫ce', 'hh', 0, '$2a$10$Apqq5cDdBQSKRO1v99qb4.rwSHC/5rFY1AAPw3BGgigGOpS9mnUga', 'http://cdn.kuailemao.lielfw.cn/userAvatar/21676863574334604.png', '这个人很懒，什么都没有留下', 'test@qq.com', '127.0.0.1', 0, '内网IP', '127.0.0.1', '内网IP', 0, '2023-12-15 10:18:28', 1, '2023-12-15 10:18:05', '2024-01-16 12:32:20', 1);
INSERT INTO `sys_user` VALUES (3, '快乐猫用户', 'qq', 0, '$2a$10$8QUAhUAD4zoLHHqcN644/.8XzN5TBJmrYIKJx.tgvttmXjh9VJi2K', 'http://cdn.kuailemao.lielfw.cn/userAvatar/21676863574334604.png', '这个人很懒，什么都没有留下', 'test@qq.com', '127.0.0.1', 0, '内网IP', '127.0.0.1', '内网IP', 0, '2023-12-19 10:17:15', 0, '2023-12-19 10:17:07', '2024-02-28 10:37:26', 1);
INSERT INTO `sys_user` VALUES (5, '快乐猫用户', 'qq', 0, '$2a$10$8QUAhUAD4zoLHHqcN644/.8XzN5TBJmrYIKJx.tgvttmXjh9VJi2K', 'http://cdn.kuailemao.lielfw.cn/userAvatar/21676863574334604.png', '这个人很懒，什么都没有留下', 'test@qq.com', '127.0.0.1', 0, '内网IP', '127.0.0.1', '内网IP', 0, '2023-12-19 10:17:15', 0, '2023-12-19 10:17:07', '2024-02-28 10:37:26', 1);
INSERT INTO `sys_user` VALUES (6, '快乐猫用户', 'qq', 0, '$2a$10$8QUAhUAD4zoLHHqcN644/.8XzN5TBJmrYIKJx.tgvttmXjh9VJi2K', 'http://cdn.kuailemao.lielfw.cn/userAvatar/21676863574334604.png', '这个人很懒，什么都没有留下', 'test@qq.com', '127.0.0.1', 0, '内网IP', '127.0.0.1', '内网IP', 0, '2023-12-19 10:17:15', 0, '2023-12-19 10:17:07', '2024-02-28 10:37:26', 1);
INSERT INTO `sys_user` VALUES (10, 'ruyu', 'ruyu', 0, '$2a$10$g4u8DiBotv.H8kvF35CgM.i1l3v0/JbBya3PvGiAghJOfOcj9RfIS', 'http://cdn.kuailemao.lielfw.cn/userAvatar/21676863574334604.png', '这个人很懒，什么都没有留下', 'test@163.com', '127.0.0.1', 0, '内网IP', '127.0.0.1', '内网IP', 0, '2023-12-11 17:21:58', 0, '2023-10-17 11:29:44', '2024-02-28 10:37:26', 1);
INSERT INTO `sys_user` VALUES (15, 'test', 'test', 0, '$2a$10$03DwMR0YxjV7KJ.D9YSPwO0qUwIT8lF1mw89SsKrUVIPVK5M2818y', 'http://cdn.kuailemao.lielfw.cn/userAvatar/21676863574334604.png', '这个人很懒，什么都没有留下', 'test@qq.com', '127.0.0.1', 0, '内网IP', '127.0.0.1', '内网IP', 0, '2023-11-30 15:50:58', 0, '2023-10-18 21:26:31', '2024-02-28 10:37:26', 1);
INSERT INTO `sys_user` VALUES (11937114, 'kuailemao', 'kuailemao', 0, '$2a$10$6gAaRvqfhNxjc5fr6e4sauX63SclNP193gnpzeLnU/nATSRS0CG4C', 'https://foruda.gitee.com/avatar/1667975309022664009/11937114_kuailemao_1667975308.png', NULL, NULL, '127.0.0.1', 1, '内网IP', '127.0.0.1', '内网IP', 1, '2023-12-22 15:14:53', 0, '2023-12-22 15:14:53', '2024-02-28 11:13:00', 0);
INSERT INTO `sys_user` VALUES (88065987, 'aaa', 'kuailemao', 0, '$2a$10$q7d2HRUMtxNKSIgGha3tg.syuaEdrU41AL1puQ26GgIVdzQurEMSy', 'https://avatars.githubusercontent.com/u/88065987?v=4', NULL, NULL, '127.0.0.1', 2, '内网IP', '127.0.0.1', '内网IP', 2, '2023-12-24 19:58:20', 0, '2023-12-24 19:58:20', '2024-02-28 11:13:20', 0);
INSERT INTO `sys_user` VALUES (88065988, '快乐猫用户', 'mao', 0, '$2a$10$mvpsj1LCUe8Vl9FPzAxzKOhuin7sJ6lQZD8r8Mo09kltVkZgKVQuG', 'http://cdn.kuailemao.lielfw.cn/userAvatar/21676863574334604.png', '这个人很懒，什么都没有留下', 'test@qq.com', '127.0.0.1', 0, '内网IP', NULL, NULL, NULL, '2024-02-28 10:54:00', 0, '2024-02-28 10:54:00', '2024-02-28 10:54:00', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_category
-- ----------------------------
INSERT INTO `t_category` VALUES (1, '生活', '2023-10-15 02:03:42', '2023-10-15 02:03:45', 0);
INSERT INTO `t_category` VALUES (2, '技术', '2023-10-15 02:03:53', '2023-10-15 02:03:56', 0);
INSERT INTO `t_category` VALUES (3, '娱乐', '2023-10-15 02:04:04', '2023-10-15 02:04:06', 0);
INSERT INTO `t_category` VALUES (4, '分类', '2024-01-04 11:23:27', '2024-01-04 11:23:27', 0);
INSERT INTO `t_category` VALUES (5, '测试', '2024-01-08 10:48:43', '2024-01-18 22:52:54', 1);
INSERT INTO `t_category` VALUES (9, '测添加', '2024-01-18 22:53:01', '2024-01-18 22:53:06', 1);
INSERT INTO `t_category` VALUES (10, '修改', '2024-01-18 22:53:12', '2024-01-18 22:53:29', 1);

-- ----------------------------
-- Table structure for t_niuniu
-- ----------------------------
DROP TABLE IF EXISTS `t_niuniu`;
CREATE TABLE `t_niuniu`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '牛牛id',
  `niuniu_id` bigint NOT NULL COMMENT '牛的编号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `category_id` bigint NOT NULL COMMENT '分类id',
  `niuniu_cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章缩略图',
  `niuniu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '牛牛名字',
  `niuniu_content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '牛牛介绍',
  `niuniu_type` tinyint NOT NULL COMMENT '类型 (1原创 2转载 3翻译)',
  `status` tinyint NOT NULL COMMENT '牛牛状态 (1公开 2私密 3草稿)',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_niuniu
-- ----------------------------
INSERT INTO `t_niuniu` VALUES (2, 0, 1, 1, 'http://cdn.kuailemao.lielfw.cn/articleCover/kuailemao11677588411382186.jpg', 'java的多态啊', '### 🍖3、多态\n![](http://43.136.78.47:9000/blog/article/articleImage/161ccca6-65c9-4ef9-9098-4a6f0446b9f9.png)\n![](http://43.136.78.47:9000/blog/article/articleImage/f34c07f4-f88a-48bf-af18-4daf44db7101.png)\n\n\n> * 面向对象的三大特性为 封装、继承、多态。最后一个特性——多态。它能使同一个操作当作用于不同的对象时，产生不同的执行结果。\n>\n> * 使用多态可以提高代码的可维护性和可扩展性\n\n#### 🍟3.1、子类到父类的转换（向上转型）\n> 子类到父类的转换被称为向上转型。（自动类型转换）\n\n **语法：**\n> < 父类型 \\> < 引用变量名 \\> = new < 子类型 \\> ( );\n```Java\n	Strive strive1 = new s1();\n```\nStrive 为父类型 strive1 为引用变量名 s1 为子类型\n\n* 父类型的引用指向子类型的对象\n\n**实现多态的三个条件如下：**\n\n> 1、继承的存在（继承是多态的继承，没有继承就没有多态）。\n>\n> > 2、子类重写父类的方法（多态下调用子类重写后的方法）。\n> >\n> > > 3、父类引用变量指向子类对象（向上转型）。\n\n#### 🍟3.2、父类到子类的转换（向下转型）\n> 父类到子类的转换被称为向上转型。（强制类型转换）\n\n**语法：**\n> < 子类型 \\> < 引用变量名 \\> = ( < 子类型 \\> ) < 父类型的引用变量 \\>;\n\n```java\n	Strive strive1 = (Strive)s1;\n```\ns1 为 父类型的引用变量，Strive 为子类型，strive1 为引用变量名\n\n#### 🍟3.3、instanceof 运算符\n**语法：**\n\n> 对象 instanceof 类或接口\n\n* 该运算符用来判断一个对象是否属于一个类或实现了一个接口，结果为 **true** 或 **false**。\n* 在强制类型转换之前通过 **instanceof** 运算符检查对象的真实类型，再进行相应的强制类型转换，这样就可以避免类型转换异常，从而提高代码的健壮性。\n\n```java\n	 if(Strive instanceof s1){  // 类型判断\n            Strive strive1 = (Strive)s1;\n        }else{\n        	System.out.println(\"Strive与s1没有关系\");\n        }\n```\n\n#### 🍟3.4、多态的优势\n* 可替换性：多态对已存在的代码具有可替换性\n* 可扩充性：多态对代码具有可扩充性。增加新的子类不影响已存在类的多态性，继承性，以及其他特性的运行和操作。实际上新加子类更容易获得多态功能。\n* 灵活性：在多态的应用中，体现了灵活多样的操作，提高了使用效率。\n* 简化性：多态简化了应用软件的代码编写和修改过程，尤其在处理大量对象的运算和操作时，这个特点尤为突出\n\n**多态的使用大多体现在实际开发中，多写代码，多用多态，慢慢自然能够体验到多态的灵活性以及多态的重要性**', 1, 1, '2022-10-15 02:26:45', '2024-01-23 23:08:12', 1);
INSERT INTO `t_niuniu` VALUES (3, 0, 1, 2, 'http://cdn.kuailemao.lielfw.cn/PicGo/idea%E8%83%8C%E6%99%AF.png', '今天出去玩了', '好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，', 1, 1, '2023-10-15 02:26:45', '2024-01-08 10:41:42', 0);
INSERT INTO `t_niuniu` VALUES (24, 0, 1, 3, 'http://cdn.kuailemao.lielfw.cn/PicGo/idea%E8%83%8C%E6%99%AF.png', '测试分类3', '好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，好好玩啊，', 1, 1, '2023-10-15 02:26:45', '2023-10-18 00:34:35', 0);
INSERT INTO `t_niuniu` VALUES (32, 0, 1, 4, 'http://43.136.78.47:9000/blog/article/articleCover/d4574635-ba1d-4c01-beda-1ad25c4db0e3.png', '测试文章', '## 这是一篇测试文章\n> 你看见这篇文章说明后台发布文章功能成功\n\n```mermaid\ngantt\ntitle A Gantt Diagram\ndateFormat  YYYY-MM-DD\nsection Section\nA task  :a1, 2014-01-01, 30d\nAnother task  :after a1, 20d\n```\n\n### 图表\n```mermaid\nclassDiagram\n  class Animal\n  Vehicle <|-- Car\n```\n', 1, 1, '2024-01-04 21:32:19', '2024-01-08 10:48:06', 0);
INSERT INTO `t_niuniu` VALUES (33, 0, 1, 2, 'http://43.136.78.47:9000/blog/article/articleCover/4580f62d-0548-47c5-8e94-42fb4dae1560.png', '图片上传', '# 测试图片上传\n## 下面是预览图\n> ![](http://43.136.78.47:9000/blog/article/articleImage/bc35310c-fe3c-487b-9d06-e2d66bec9a3a.gif)\n### 剪切上传\n![](http://43.136.78.47:9000/blog/article/articleImage/1f0b3ec4-91aa-4165-b1f8-060e89baf783.png)\n![](http://43.136.78.47:9000/blog/article/articleImage/0d45c380-4f54-42e6-8c94-fb589c6444f2.gif)\n', 1, 1, '2024-01-05 16:49:37', '2024-01-09 23:53:42', 1);
INSERT INTO `t_niuniu` VALUES (37, 0, 1, 1, 'http://43.136.78.47:9000/blog/article/articleCover/72494e09-3fae-4a7d-bfe9-37108bfd3766.png', '测是AA', '顶顶顶', 1, 1, '2024-01-08 10:39:36', '2024-01-09 23:47:46', 1);
INSERT INTO `t_niuniu` VALUES (38, 0, 1, 5, 'http://43.136.78.47:9000/blog/article/articleCover/152113cc-ea0e-451e-8912-4eea8ba5096f.png', '测试添加', '| col | col | col | col |\n| - | - | - | - |\n| content | content | content | content |\n| content | content | content | content |\n| content | content | content | content |\n```mermaid\nerDiagram\n  CAR ||--o{ NAMED-DRIVER : allows\n  PERSON ||--o{ NAMED-DRIVER : is\n```\n```mermaid\njourney\n  title My working day\n  section Go to work\n    Make tea: 5: Me\n    Go upstairs: 3: Me\n    Do work: 1: Me, Cat\n  section Go home\n    Go downstairs: 5: Me\n    Sit down: 5: Me\n```\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n', 1, 1, '2024-01-08 10:49:50', '2024-01-09 23:44:35', 1);
INSERT INTO `t_niuniu` VALUES (39, 0, 1, 2, 'http://43.136.78.47:9000/blog/article/articleCover/ba380480-c8d6-40aa-822a-6105e03eb259.jpg', '测试删除', '删除', 1, 1, '2024-01-09 23:51:49', '2024-01-09 23:53:42', 1);
INSERT INTO `t_niuniu` VALUES (40, 0, 1, 1, 'http://43.136.78.47:9000/blog/article/articleCover/cfce7f30-f9e7-4ca4-81b9-c0831cc9c221.png', '测试', '新文章', 1, 1, '2024-02-28 14:05:36', '2024-02-28 14:05:36', 0);

-- ----------------------------
-- Table structure for t_niuniu_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_niuniu_tag`;
CREATE TABLE `t_niuniu_tag`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '关系表id',
  `article_id` bigint UNSIGNED NOT NULL COMMENT '文章id',
  `tag_id` bigint NOT NULL COMMENT '标签id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除，1：已删除）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 80 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_niuniu_tag
-- ----------------------------
INSERT INTO `t_niuniu_tag` VALUES (1, 1, 1, '2023-10-15 02:04:40', 0);
INSERT INTO `t_niuniu_tag` VALUES (2, 1, 2, '2023-10-15 02:04:47', 0);
INSERT INTO `t_niuniu_tag` VALUES (3, 1, 3, '2023-10-15 02:04:57', 0);
INSERT INTO `t_niuniu_tag` VALUES (28, 24, 2, '2023-10-29 23:07:40', 0);
INSERT INTO `t_niuniu_tag` VALUES (29, 2, 3, '2023-10-15 02:04:57', 1);
INSERT INTO `t_niuniu_tag` VALUES (30, 2, 1, '2023-10-15 02:04:57', 1);
INSERT INTO `t_niuniu_tag` VALUES (31, 3, 3, '2023-10-15 02:04:57', 0);
INSERT INTO `t_niuniu_tag` VALUES (32, 3, 5, '2023-10-30 10:16:22', 0);
INSERT INTO `t_niuniu_tag` VALUES (33, 24, 4, '2023-10-30 10:16:36', 0);
INSERT INTO `t_niuniu_tag` VALUES (34, 27, 2, '2024-01-04 17:11:13', 0);
INSERT INTO `t_niuniu_tag` VALUES (35, 27, 3, '2024-01-04 17:11:13', 0);
INSERT INTO `t_niuniu_tag` VALUES (36, 27, 4, '2024-01-04 17:11:13', 0);
INSERT INTO `t_niuniu_tag` VALUES (37, 28, 2, '2024-01-04 17:22:13', 0);
INSERT INTO `t_niuniu_tag` VALUES (38, 28, 3, '2024-01-04 17:22:13', 0);
INSERT INTO `t_niuniu_tag` VALUES (39, 28, 4, '2024-01-04 17:22:13', 0);
INSERT INTO `t_niuniu_tag` VALUES (40, 29, 5, '2024-01-04 17:46:31', 0);
INSERT INTO `t_niuniu_tag` VALUES (41, 29, 4, '2024-01-04 17:46:31', 0);
INSERT INTO `t_niuniu_tag` VALUES (42, 30, 2, '2024-01-04 17:47:07', 0);
INSERT INTO `t_niuniu_tag` VALUES (43, 31, 3, '2024-01-04 17:47:35', 0);
INSERT INTO `t_niuniu_tag` VALUES (44, 31, 5, '2024-01-04 17:47:35', 0);
INSERT INTO `t_niuniu_tag` VALUES (45, 32, 3, '2024-01-04 21:32:19', 0);
INSERT INTO `t_niuniu_tag` VALUES (46, 32, 2, '2024-01-04 21:32:19', 0);
INSERT INTO `t_niuniu_tag` VALUES (47, 33, 2, '2024-01-05 16:49:37', 1);
INSERT INTO `t_niuniu_tag` VALUES (48, 33, 3, '2024-01-05 16:49:37', 1);
INSERT INTO `t_niuniu_tag` VALUES (49, 2, 1, '2024-01-08 10:30:26', 1);
INSERT INTO `t_niuniu_tag` VALUES (50, 2, 3, '2024-01-08 10:30:26', 1);
INSERT INTO `t_niuniu_tag` VALUES (51, 2, 1, '2024-01-08 10:30:41', 1);
INSERT INTO `t_niuniu_tag` VALUES (52, 2, 3, '2024-01-08 10:30:41', 1);
INSERT INTO `t_niuniu_tag` VALUES (53, 2, 1, '2024-01-08 10:31:48', 1);
INSERT INTO `t_niuniu_tag` VALUES (54, 2, 3, '2024-01-08 10:31:48', 1);
INSERT INTO `t_niuniu_tag` VALUES (55, 34, 5, '2024-01-08 10:37:04', 0);
INSERT INTO `t_niuniu_tag` VALUES (56, 35, 5, '2024-01-08 10:37:19', 0);
INSERT INTO `t_niuniu_tag` VALUES (57, 36, 3, '2024-01-08 10:38:13', 0);
INSERT INTO `t_niuniu_tag` VALUES (58, 37, 4, '2024-01-08 10:39:36', 1);
INSERT INTO `t_niuniu_tag` VALUES (59, 37, 2, '2024-01-08 10:39:36', 1);
INSERT INTO `t_niuniu_tag` VALUES (60, 3, 3, '2024-01-08 10:41:42', 0);
INSERT INTO `t_niuniu_tag` VALUES (61, 3, 5, '2024-01-08 10:41:42', 0);
INSERT INTO `t_niuniu_tag` VALUES (62, 37, 2, '2024-01-08 10:42:07', 1);
INSERT INTO `t_niuniu_tag` VALUES (63, 37, 4, '2024-01-08 10:42:07', 1);
INSERT INTO `t_niuniu_tag` VALUES (64, 37, 2, '2024-01-08 10:43:01', 1);
INSERT INTO `t_niuniu_tag` VALUES (65, 37, 4, '2024-01-08 10:43:01', 1);
INSERT INTO `t_niuniu_tag` VALUES (66, 37, 2, '2024-01-08 10:43:19', 1);
INSERT INTO `t_niuniu_tag` VALUES (67, 37, 4, '2024-01-08 10:43:19', 1);
INSERT INTO `t_niuniu_tag` VALUES (68, 32, 2, '2024-01-08 10:44:57', 0);
INSERT INTO `t_niuniu_tag` VALUES (69, 32, 3, '2024-01-08 10:44:57', 0);
INSERT INTO `t_niuniu_tag` VALUES (70, 37, 2, '2024-01-08 10:47:44', 1);
INSERT INTO `t_niuniu_tag` VALUES (71, 37, 4, '2024-01-08 10:47:44', 1);
INSERT INTO `t_niuniu_tag` VALUES (72, 32, 2, '2024-01-08 10:48:06', 0);
INSERT INTO `t_niuniu_tag` VALUES (73, 32, 3, '2024-01-08 10:48:06', 0);
INSERT INTO `t_niuniu_tag` VALUES (74, 38, 4, '2024-01-08 10:49:50', 1);
INSERT INTO `t_niuniu_tag` VALUES (75, 38, 3, '2024-01-08 10:49:50', 1);
INSERT INTO `t_niuniu_tag` VALUES (76, 2, 1, '2024-01-08 10:57:52', 1);
INSERT INTO `t_niuniu_tag` VALUES (77, 2, 3, '2024-01-08 10:57:52', 1);
INSERT INTO `t_niuniu_tag` VALUES (78, 39, 2, '2024-01-09 23:51:49', 1);
INSERT INTO `t_niuniu_tag` VALUES (79, 40, 2, '2024-02-28 14:05:36', 0);

-- ----------------------------
-- Table structure for t_niuniu_weight
-- ----------------------------
DROP TABLE IF EXISTS `t_niuniu_weight`;
CREATE TABLE `t_niuniu_weight`  (
  `id` int NOT NULL COMMENT '主键',
  `niuniu_id` int NOT NULL COMMENT '牛牛id',
  `niuniu_weight` double NOT NULL COMMENT '牛牛体重',
  `creat_time` datetime NOT NULL COMMENT '体重时间戳',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_niuniu_weight
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_tag
-- ----------------------------
INSERT INTO `t_tag` VALUES (1, 'java', '2023-10-15 02:02:28', '2023-10-15 02:02:30', 0);
INSERT INTO `t_tag` VALUES (2, 'python', '2023-10-15 02:02:53', '2023-10-15 02:02:55', 0);
INSERT INTO `t_tag` VALUES (3, 'c#', '2023-10-15 02:03:06', '2023-10-15 02:03:09', 0);
INSERT INTO `t_tag` VALUES (4, 'c++', '2023-10-15 02:03:23', '2023-10-15 02:03:25', 0);
INSERT INTO `t_tag` VALUES (5, '开心', '2023-10-15 23:17:19', '2023-10-15 23:17:22', 0);

SET FOREIGN_KEY_CHECKS = 1;
