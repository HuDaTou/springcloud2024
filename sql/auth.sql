/*
 Navicat Premium Data Transfer

 Source Server         : postgre
 Source Server Type    : PostgreSQL
 Source Server Version : 160009 (160009)
 Source Host           : localhost:5432
 Source Catalog        : cloud
 Source Schema         : auth

 Target Server Type    : PostgreSQL
 Target Server Version : 160009 (160009)
 File Encoding         : 65001

 Date: 15/06/2026 22:32:30
*/


-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS "auth"."sys_dict_data";
CREATE TABLE "auth"."sys_dict_data" (
  "dict_code" int8 NOT NULL,
  "dict_sort" int4 DEFAULT 0,
  "dict_label" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "dict_value" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "dict_type" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "css_class" varchar(100) COLLATE "pg_catalog"."default",
  "list_class" varchar(100) COLLATE "pg_catalog"."default",
  "is_default" char(1) COLLATE "pg_catalog"."default" DEFAULT 'N'::bpchar,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON TABLE "auth"."sys_dict_data" IS '字典数据表';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO "auth"."sys_dict_data" VALUES (1, 1, '男', '0', 'sys_user_sex', NULL, NULL, 'Y', '0', 'admin', '2026-05-16 10:29:52.834556', '', NULL, NULL);
INSERT INTO "auth"."sys_dict_data" VALUES (2, 2, '女', '1', 'sys_user_sex', NULL, NULL, 'N', '0', 'admin', '2026-05-16 10:29:52.834556', '', NULL, NULL);
INSERT INTO "auth"."sys_dict_data" VALUES (3, 3, '未知', '2', 'sys_user_sex', NULL, NULL, 'N', '0', 'admin', '2026-05-16 10:29:52.834556', '', NULL, NULL);
INSERT INTO "auth"."sys_dict_data" VALUES (4, 1, '显示', '0', 'sys_show_hide', NULL, NULL, 'Y', '0', 'admin', '2026-05-16 10:29:52.834556', '', NULL, NULL);
INSERT INTO "auth"."sys_dict_data" VALUES (5, 2, '隐藏', '1', 'sys_show_hide', NULL, NULL, 'N', '0', 'admin', '2026-05-16 10:29:52.834556', '', NULL, NULL);
INSERT INTO "auth"."sys_dict_data" VALUES (6, 1, '正常', '0', 'sys_normal_disable', NULL, NULL, 'Y', '0', 'admin', '2026-05-16 10:29:52.834556', '', NULL, NULL);
INSERT INTO "auth"."sys_dict_data" VALUES (7, 2, '停用', '1', 'sys_normal_disable', NULL, NULL, 'N', '0', 'admin', '2026-05-16 10:29:52.834556', '', NULL, NULL);
INSERT INTO "auth"."sys_dict_data" VALUES (8, 1, '是', 'Y', 'sys_yes_no', NULL, NULL, 'Y', '0', 'admin', '2026-05-16 10:29:52.834556', '', NULL, NULL);
INSERT INTO "auth"."sys_dict_data" VALUES (9, 2, '否', 'N', 'sys_yes_no', NULL, NULL, 'N', '0', 'admin', '2026-05-16 10:29:52.834556', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS "auth"."sys_dict_type";
CREATE TABLE "auth"."sys_dict_type" (
  "dict_id" int8 NOT NULL,
  "dict_name" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "dict_type" varchar(100) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "status" char(1) COLLATE "pg_catalog"."default" DEFAULT '0'::bpchar,
  "create_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "create_time" timestamp(6),
  "update_by" varchar(64) COLLATE "pg_catalog"."default" DEFAULT ''::character varying,
  "update_time" timestamp(6),
  "remark" varchar(500) COLLATE "pg_catalog"."default"
)
;
COMMENT ON TABLE "auth"."sys_dict_type" IS '字典类型表';

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO "auth"."sys_dict_type" VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2026-05-16 10:29:52.833424', '', NULL, NULL);
INSERT INTO "auth"."sys_dict_type" VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2026-05-16 10:29:52.833424', '', NULL, NULL);
INSERT INTO "auth"."sys_dict_type" VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2026-05-16 10:29:52.833424', '', NULL, NULL);
INSERT INTO "auth"."sys_dict_type" VALUES (4, '系统是否', 'sys_yes_no', '0', 'admin', '2026-05-16 10:29:52.833424', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS "auth"."sys_log";
CREATE TABLE "auth"."sys_log" (
  "id" int8 NOT NULL,
  "module" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "operation" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "user_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "ip" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "address" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "state" int2 NOT NULL,
  "method" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "req_parameter" text COLLATE "pg_catalog"."default",
  "req_mapping" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "exception" text COLLATE "pg_catalog"."default",
  "return_parameter" text COLLATE "pg_catalog"."default",
  "req_address" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "time" int8 NOT NULL,
  "description" varchar(100) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6) NOT NULL,
  "update_time" timestamp(6) NOT NULL,
  "is_deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON TABLE "auth"."sys_log" IS '操作日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS "auth"."sys_login_log";
CREATE TABLE "auth"."sys_login_log" (
  "id" int8 NOT NULL,
  "user_name" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "ip" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "address" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "browser" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "os" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "type" int2 NOT NULL,
  "state" int2 NOT NULL,
  "message" text COLLATE "pg_catalog"."default" NOT NULL,
  "create_time" timestamp(6) NOT NULL,
  "update_time" timestamp(6) NOT NULL,
  "is_deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON TABLE "auth"."sys_login_log" IS '登录日志表';

-- ----------------------------
-- Records of sys_login_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS "auth"."sys_menu";
CREATE TABLE "auth"."sys_menu" (
  "id" int8 NOT NULL,
  "title" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "icon" varchar(50) COLLATE "pg_catalog"."default",
  "path" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "component" varchar(255) COLLATE "pg_catalog"."default",
  "redirect" varchar(255) COLLATE "pg_catalog"."default",
  "affix" int2 DEFAULT 0,
  "parent_id" int8,
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "hide_in_menu" int2 DEFAULT 0,
  "url" varchar(255) COLLATE "pg_catalog"."default",
  "hide_in_breadcrumb" int2 DEFAULT 1,
  "hide_children_in_menu" int2 DEFAULT 1,
  "keep_alive" int2 DEFAULT 1,
  "target" varchar(255) COLLATE "pg_catalog"."default",
  "is_disable" bool NOT NULL DEFAULT false,
  "order_num" int4 NOT NULL DEFAULT 1,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "is_deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON TABLE "auth"."sys_menu" IS '菜单表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO "auth"."sys_menu" VALUES (1, '系统管理', 'lucide:settings', '/system', 'BasicLayout', '/system/menu', 0, NULL, 'System', 0, NULL, 1, 1, 1, NULL, 'f', 2, '2026-05-16 10:29:52.835727', '2026-05-16 10:29:52.835727', 'f');
INSERT INTO "auth"."sys_menu" VALUES (3, '用户管理', 'lucide:users', '/system/user', 'system/user/list', '', 0, 1, 'SystemUser', 0, NULL, 1, 1, 1, NULL, 'f', 0, '2026-05-16 10:29:52.835727', '2026-05-16 10:29:52.835727', 'f');
INSERT INTO "auth"."sys_menu" VALUES (2, '菜单管理', 'lucide:menu', '/system/menu', 'system/menu/list', '', 0, 1, 'SystemMenu', 0, NULL, 1, 1, 1, NULL, 'f', 1, '2026-05-16 10:29:52.835727', '2026-05-16 10:29:52.835727', 'f');
INSERT INTO "auth"."sys_menu" VALUES (24, '权限管理', 'lucide:shield-check', '/system/permission', 'system/permission/list', '', 0, 1, 'SystemPermission', 0, NULL, 1, 1, 1, NULL, 'f', 4, '2026-05-16 10:29:52.835727', '2026-05-16 10:29:52.835727', 'f');
INSERT INTO "auth"."sys_menu" VALUES (90001, '仪表盘', 'lucide:layout-dashboard', '/dashboard', 'BasicLayout', NULL, 0, NULL, 'Dashboard', 0, NULL, 1, 1, 1, NULL, 'f', -1, NULL, NULL, 'f');
INSERT INTO "auth"."sys_menu" VALUES (90002, '分析页', 'lucide:area-chart', '/dashboard/analytics', 'dashboard/analytics/index', NULL, 1, 90001, 'Analytics', 0, NULL, 1, 1, 1, NULL, 'f', 0, NULL, NULL, 'f');
INSERT INTO "auth"."sys_menu" VALUES (90003, '工作台', 'carbon:workspace', '/dashboard/workspace', 'dashboard/workspace/index', NULL, 0, 90001, 'Workspace', 0, NULL, 1, 1, 1, NULL, 'f', 1, NULL, NULL, 'f');
INSERT INTO "auth"."sys_menu" VALUES (90004, '角色管理', 'lucide:user-cog', '/system/role', 'system/role/list', NULL, 0, 1, 'SystemRole', 0, NULL, 1, 1, 1, NULL, 'f', 3, NULL, NULL, 'f');

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS "auth"."sys_permission";
CREATE TABLE "auth"."sys_permission" (
  "id" int8 NOT NULL,
  "permission_desc" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "permission_key" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "menu_id" int8,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "is_deleted" bool NOT NULL DEFAULT false,
  "category" varchar(255) COLLATE "pg_catalog"."default",
  "http_method" varchar(50) COLLATE "pg_catalog"."default",
  "path" varchar(500) COLLATE "pg_catalog"."default",
  "service_name" varchar(100) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "auth"."sys_permission"."id" IS '权限表id';
COMMENT ON COLUMN "auth"."sys_permission"."permission_desc" IS '描述';
COMMENT ON COLUMN "auth"."sys_permission"."permission_key" IS '权限字符';
COMMENT ON COLUMN "auth"."sys_permission"."menu_id" IS '菜单id';
COMMENT ON COLUMN "auth"."sys_permission"."create_time" IS '创建时间';
COMMENT ON COLUMN "auth"."sys_permission"."update_time" IS '更新时间';
COMMENT ON COLUMN "auth"."sys_permission"."is_deleted" IS '逻辑删除';
COMMENT ON COLUMN "auth"."sys_permission"."category" IS '功能分类';
COMMENT ON COLUMN "auth"."sys_permission"."http_method" IS '请求方式
';
COMMENT ON COLUMN "auth"."sys_permission"."path" IS '请求路径';
COMMENT ON COLUMN "auth"."sys_permission"."service_name" IS '所属服务名称';
COMMENT ON TABLE "auth"."sys_permission" IS '权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO "auth"."sys_permission" VALUES (2065679790173609985, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065679790173609986, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065679790173609987, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207887888386, '获取用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证服务', 'GET', '/cloud-auth/auth/codes', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207892082689, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/add', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207892082690, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207896276993, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207896276994, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207896276995, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207896276996, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207900471297, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207900471298, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207900471299, 'getAllRoles', 'auth:role:list', NULL, NULL, NULL, 't', 'RoleController', 'GET', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207904665601, 'createRole', 'auth:role:add', NULL, NULL, NULL, 't', 'RoleController', 'POST', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207904665602, 'updateRole', 'auth:role:edit', NULL, NULL, NULL, 't', 'RoleController', 'PUT', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207904665603, 'deleteRole', 'auth:role:delete', NULL, NULL, NULL, 't', 'RoleController', 'DELETE', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207904665604, 'getPermissionIdsByRoleId', 'auth:role:permission:query', NULL, NULL, NULL, 't', 'RolePermissionController', 'GET', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207904665605, 'assignPermissionsToRole', 'auth:role:permission:edit', NULL, NULL, NULL, 't', 'RolePermissionController', 'PUT', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207904665606, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/update/status', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207904665607, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/cloud-auth/user/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207908859906, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/admin/create', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207908859907, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/list', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207908859908, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/search', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065682207913054210, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/details/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966128238594, '获取用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966132432897, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/add', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966132432898, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966136627202, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966136627203, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966136627204, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966140821505, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966140821506, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966145015810, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966145015811, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966145015812, '创建角色', 'auth:role:add', NULL, NULL, NULL, 't', '角色管理', 'POST', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966145015813, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966145015814, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 't', '角色管理', 'DELETE', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966145015815, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 't', '角色权限管理', 'GET', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966149210113, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 't', '角色权限管理', 'PUT', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966149210114, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/list', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966149210115, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/search', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966149210116, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/update/status', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966153404417, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/details/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966153404418, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/cloud-auth/user/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065738966153404419, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/admin/create', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066076174559326210, '获取当前用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证管理', 'GET', '/unknown/auth/codes', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174571909122, '根据用户ID获取权限码', 'auth:user:query', NULL, NULL, NULL, 't', '认证管理', 'GET', '/unknown/auth/codes/user', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174576103425, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/unknown/blackList/add', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174584492034, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/unknown/blackList/update', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174584492035, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/unknown/blackList/delete', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174588686338, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/unknown/blackList/getBlackListing', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174592880641, '获取菜单详情', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/unknown/menu/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174597074946, '创建菜单', 'auth:menu:add', NULL, NULL, NULL, 't', '菜单管理', 'POST', '/unknown/menu', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2065810775427031042, '获取用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775435419650, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/add', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775439613954, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775443808257, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775448002562, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775452196866, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066076174605463553, '更新菜单', 'auth:menu:edit', NULL, NULL, NULL, 't', '菜单管理', 'PUT', '/unknown/menu/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174605463554, '删除菜单', 'auth:menu:delete', NULL, NULL, NULL, 't', '菜单管理', 'DELETE', '/unknown/menu/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174609657858, '获取菜单列表（平铺）', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/unknown/menu', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174609657859, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/unknown/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174609657860, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/unknown/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174618046465, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/unknown/permissions/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174622240770, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/unknown/permissions/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174622240771, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 't', '角色管理', 'GET', '/unknown/roles', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174622240772, '创建角色', 'auth:role:add', NULL, NULL, NULL, 't', '角色管理', 'POST', '/unknown/roles', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174626435073, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/unknown/roles/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174630629378, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 't', '角色管理', 'DELETE', '/unknown/roles/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174634823682, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 't', '角色权限管理', 'GET', '/unknown/roles/{roleId}/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174634823683, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 't', '角色权限管理', 'PUT', '/unknown/roles/{roleId}/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174639017985, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/unknown/user/update/status', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174643212289, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/unknown/user/delete', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174643212290, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/unknown/user/admin/create', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174647406593, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/unknown/user/list', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174647406594, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/unknown/user/search', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076174651600898, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/unknown/user/details/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585127084034, '获取当前用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证管理', 'GET', '/unknown/auth/codes', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585135472641, '根据用户ID获取权限码', 'auth:user:query', NULL, NULL, NULL, 't', '认证管理', 'GET', '/unknown/auth/codes/user', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585139666945, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/unknown/blackList/add', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585143861249, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/unknown/blackList/update', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585148055553, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/unknown/blackList/delete', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585152249858, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/unknown/blackList/getBlackListing', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585156444161, '获取菜单详情', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/unknown/menu/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585160638465, '创建菜单', 'auth:menu:add', NULL, NULL, NULL, 't', '菜单管理', 'POST', '/unknown/menu', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585169027073, '更新菜单', 'auth:menu:edit', NULL, NULL, NULL, 't', '菜单管理', 'PUT', '/unknown/menu/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585169027074, '删除菜单', 'auth:menu:delete', NULL, NULL, NULL, 't', '菜单管理', 'DELETE', '/unknown/menu/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585173221377, '获取菜单列表（平铺）', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/unknown/menu', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585177415681, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/unknown/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585181609985, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/unknown/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585185804290, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/unknown/permissions/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585189998593, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/unknown/permissions/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585189998594, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 't', '角色管理', 'GET', '/unknown/roles', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585198387202, '创建角色', 'auth:role:add', NULL, NULL, NULL, 't', '角色管理', 'POST', '/unknown/roles', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585206775809, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/unknown/roles/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585206775810, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 't', '角色管理', 'DELETE', '/unknown/roles/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585210970114, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 't', '角色权限管理', 'GET', '/unknown/roles/{roleId}/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585215164417, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 't', '角色权限管理', 'PUT', '/unknown/roles/{roleId}/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585215164418, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/unknown/user/update/status', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585215164419, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/unknown/user/delete', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585223553026, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/unknown/user/admin/create', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585223553027, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/unknown/user/list', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585227747330, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/unknown/user/search', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066076585227747331, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/unknown/user/details/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241692585986, '获取当前用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 'f', '认证管理', 'GET', '/unknown/auth/codes', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241705168897, '根据用户ID获取权限码', 'auth:user:query', NULL, NULL, NULL, 'f', '认证管理', 'GET', '/unknown/auth/codes/user', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241705168898, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 'f', 'BlackListController', 'POST', '/unknown/blackList/add', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241705168899, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 'f', 'BlackListController', 'PUT', '/unknown/blackList/update', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241709363201, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 'f', 'BlackListController', 'POST', '/unknown/blackList/getBlackListing', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241709363202, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 'f', 'BlackListController', 'DELETE', '/unknown/blackList/delete', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241713557505, '创建菜单', 'auth:menu:add', NULL, NULL, NULL, 'f', '菜单管理', 'POST', '/unknown/menu', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241717751809, '更新菜单', 'auth:menu:edit', NULL, NULL, NULL, 'f', '菜单管理', 'PUT', '/unknown/menu/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241721946114, '删除菜单', 'auth:menu:delete', NULL, NULL, NULL, 'f', '菜单管理', 'DELETE', '/unknown/menu/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241721946115, '获取菜单列表（平铺）', 'auth:menu:list', NULL, NULL, NULL, 'f', '菜单管理', 'GET', '/unknown/menu', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241726140417, '获取菜单详情', 'auth:menu:list', NULL, NULL, NULL, 'f', '菜单管理', 'GET', '/unknown/menu/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241730334722, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 'f', '权限管理', 'GET', '/unknown/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241734529025, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 'f', '权限管理', 'POST', '/unknown/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241738723329, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 'f', '权限管理', 'PUT', '/unknown/permissions/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241738723330, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 'f', '权限管理', 'DELETE', '/unknown/permissions/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241742917634, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 'f', '角色管理', 'GET', '/unknown/roles', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241747111938, '创建角色', 'auth:role:add', NULL, NULL, NULL, 'f', '角色管理', 'POST', '/unknown/roles', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241751306242, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 'f', '角色管理', 'PUT', '/unknown/roles/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241755500545, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 'f', '角色管理', 'DELETE', '/unknown/roles/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241759694849, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 'f', '角色权限管理', 'GET', '/unknown/roles/{roleId}/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241759694850, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 'f', '角色权限管理', 'PUT', '/unknown/roles/{roleId}/permissions', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241759694851, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 'f', '用户管理', 'GET', '/unknown/user/list', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241763889154, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 'f', '用户管理', 'POST', '/unknown/user/search', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241763889155, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 'f', '用户管理', 'POST', '/unknown/user/update/status', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241768083458, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 'f', '用户管理', 'GET', '/unknown/user/details/{id}', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241768083459, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 'f', '用户管理', 'DELETE', '/unknown/user/delete', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2066078241776472066, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 'f', '用户管理', 'POST', '/unknown/user/admin/create', 'unknown-service');
INSERT INTO "auth"."sys_permission" VALUES (2065810775452196865, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775456391169, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775460585473, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775460585474, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775468974082, '创建角色', 'auth:role:add', NULL, NULL, NULL, 't', '角色管理', 'POST', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775473168385, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775481556994, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 't', '角色管理', 'DELETE', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775481556995, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 't', '角色权限管理', 'GET', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775485751297, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 't', '角色权限管理', 'PUT', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775485751298, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/list', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775489945601, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/search', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775502528514, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/update/status', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775502528515, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/details/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775502528516, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/cloud-auth/user/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2065810775502528517, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/admin/create', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161148727297, '获取当前用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161161310209, '根据用户ID获取权限码', 'auth:user:query', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes/user', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161161310210, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/add', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161169698817, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161169698818, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161173893122, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161173893123, '获取菜单详情', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161178087426, '创建菜单', 'auth:menu:add', NULL, NULL, NULL, 't', '菜单管理', 'POST', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161182281729, '更新菜单', 'auth:menu:edit', NULL, NULL, NULL, 't', '菜单管理', 'PUT', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161182281730, '删除菜单', 'auth:menu:delete', NULL, NULL, NULL, 't', '菜单管理', 'DELETE', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161186476033, '获取菜单列表（平铺）', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161186476034, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161190670337, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161194864641, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161194864642, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161199058945, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161199058946, '创建角色', 'auth:role:add', NULL, NULL, NULL, 't', '角色管理', 'POST', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161203253249, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161207447553, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 't', '角色管理', 'DELETE', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161207447554, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 't', '角色权限管理', 'GET', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161207447555, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 't', '角色权限管理', 'PUT', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161207447556, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/update/status', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161211641858, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/cloud-auth/user/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161211641859, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/admin/create', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161220030466, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/list', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161224224770, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/search', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066080161228419073, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/details/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597770997762, '获取当前用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597779386370, '根据用户ID获取权限码', 'auth:user:query', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes/user', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597783580673, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/add', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597783580674, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597787774977, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597787774978, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597791969282, '创建菜单', 'auth:menu:add', NULL, NULL, NULL, 't', '菜单管理', 'POST', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597791969283, '更新菜单', 'auth:menu:edit', NULL, NULL, NULL, 't', '菜单管理', 'PUT', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597796163585, '删除菜单', 'auth:menu:delete', NULL, NULL, NULL, 't', '菜单管理', 'DELETE', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597796163586, '获取菜单列表（平铺）', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597800357889, '获取菜单详情', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597804552193, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597808746497, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597808746498, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597812940802, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597812940803, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597817135106, '创建角色', 'auth:role:add', NULL, NULL, NULL, 't', '角色管理', 'POST', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597825523714, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597825523715, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 't', '角色管理', 'DELETE', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597825523716, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 't', '角色权限管理', 'GET', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597825523717, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 't', '角色权限管理', 'PUT', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597825523718, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/list', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597829718018, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/search', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597829718019, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/update/status', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597833912322, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/details/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597833912323, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/cloud-auth/user/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066101597833912324, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/admin/create', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907323076609, '获取当前用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907339853825, '根据用户ID获取权限码', 'auth:user:query', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes/user', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907339853826, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/add', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907348242433, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907352436738, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907352436739, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907356631042, '创建菜单', 'auth:menu:add', NULL, NULL, NULL, 't', '菜单管理', 'POST', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907360825346, '更新菜单', 'auth:menu:edit', NULL, NULL, NULL, 't', '菜单管理', 'PUT', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907365019650, '删除菜单', 'auth:menu:delete', NULL, NULL, NULL, 't', '菜单管理', 'DELETE', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907365019651, '获取菜单列表（平铺）', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907369213954, '获取菜单详情', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907373408258, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907373408259, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907377602562, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907381796865, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907381796866, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907385991170, '创建角色', 'auth:role:add', NULL, NULL, NULL, 't', '角色管理', 'POST', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907385991171, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907385991172, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 't', '角色管理', 'DELETE', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907385991173, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 't', '角色权限管理', 'GET', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907390185473, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 't', '角色权限管理', 'PUT', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907394379777, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/list', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907394379778, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/search', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907398574081, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/update/status', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907398574082, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/details/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907402768386, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/cloud-auth/user/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066158907406962689, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/admin/create', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845797773313, '获取当前用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845806161922, '根据用户ID获取权限码', 'auth:user:query', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes/user', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845810356225, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/add', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845818744834, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845818744835, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845822939138, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845827133442, '创建菜单', 'auth:menu:add', NULL, NULL, NULL, 't', '菜单管理', 'POST', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845831327746, '更新菜单', 'auth:menu:edit', NULL, NULL, NULL, 't', '菜单管理', 'PUT', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845835522050, '删除菜单', 'auth:menu:delete', NULL, NULL, NULL, 't', '菜单管理', 'DELETE', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845839716354, '获取菜单列表（平铺）', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845839716355, '获取菜单详情', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845848104961, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845852299266, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845856493570, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845856493571, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845856493572, '获取权限树', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions/tree', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845856493573, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845869076482, '创建角色', 'auth:role:add', NULL, NULL, NULL, 't', '角色管理', 'POST', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845869076483, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845873270785, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 't', '角色管理', 'DELETE', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845873270786, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845873270787, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845873270788, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/list', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845877465089, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/search', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845877465090, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/update/status', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845877465091, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/details/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845881659393, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/cloud-auth/user/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066503845881659394, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/admin/create', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525221638145, '获取当前用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525230026753, '根据用户ID获取权限码', 'auth:user:query', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes/user', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525230026754, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/add', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525234221058, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525238415362, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525242609666, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525246803969, '创建菜单', 'auth:menu:add', NULL, NULL, NULL, 't', '菜单管理', 'POST', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525246803970, '更新菜单', 'auth:menu:edit', NULL, NULL, NULL, 't', '菜单管理', 'PUT', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525255192577, '删除菜单', 'auth:menu:delete', NULL, NULL, NULL, 't', '菜单管理', 'DELETE', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525255192578, '获取菜单列表（平铺）', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525259386881, '获取菜单详情', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525263581186, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525276164097, '创建角色', 'auth:role:add', NULL, NULL, NULL, 't', '角色管理', 'POST', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525263581187, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525267775489, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525271969794, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525271969795, '获取权限树', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions/tree', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525271969796, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525276164098, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525276164099, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 't', '角色管理', 'DELETE', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525280358402, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525280358403, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525280358404, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/list', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525284552706, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/search', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525284552707, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/update/status', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525284552708, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/details/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525288747010, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/cloud-auth/user/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066507525288747011, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/admin/create', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216681279489, '获取当前用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216693862402, '根据用户ID获取权限码', 'auth:user:query', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes/user', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216693862403, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/add', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216702251010, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216706445313, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216706445314, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216710639617, '获取菜单详情', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216714833922, '创建菜单', 'auth:menu:add', NULL, NULL, NULL, 't', '菜单管理', 'POST', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216719028225, '更新菜单', 'auth:menu:edit', NULL, NULL, NULL, 't', '菜单管理', 'PUT', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216719028226, '删除菜单', 'auth:menu:delete', NULL, NULL, NULL, 't', '菜单管理', 'DELETE', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216719028227, '获取菜单列表（平铺）', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216723222530, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216731611138, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216731611139, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216731611140, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216731611141, '获取权限树', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions/tree', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216739999745, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216744194050, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216744194051, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216744194052, '创建角色', 'auth:role:add', NULL, NULL, NULL, 't', '角色管理', 'POST', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216748388354, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216748388355, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 't', '角色管理', 'DELETE', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216752582658, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/update/status', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216752582659, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/cloud-auth/user/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216756776961, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/admin/create', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216756776962, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/list', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216760971266, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/search', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066510216760971267, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/details/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557461835777, '获取当前用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557470224386, '根据用户ID获取权限码', 'auth:user:query', NULL, NULL, NULL, 't', '认证管理', 'GET', '/cloud-auth/auth/codes/user', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557474418689, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/add', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557478612994, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 't', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557482807297, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 't', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557487001601, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 't', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557487001602, '获取菜单详情', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557491195905, '创建菜单', 'auth:menu:add', NULL, NULL, NULL, 't', '菜单管理', 'POST', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557491195906, '更新菜单', 'auth:menu:edit', NULL, NULL, NULL, 't', '菜单管理', 'PUT', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557491195907, '删除菜单', 'auth:menu:delete', NULL, NULL, NULL, 't', '菜单管理', 'DELETE', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557495390210, '获取菜单列表（平铺）', 'auth:menu:list', NULL, NULL, NULL, 't', '菜单管理', 'GET', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557503778817, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557512167426, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 't', '权限管理', 'POST', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557512167427, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 't', '权限管理', 'PUT', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557516361730, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 't', '权限管理', 'DELETE', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557516361731, '获取权限树', 'auth:permission:list', NULL, NULL, NULL, 't', '权限管理', 'GET', '/cloud-auth/permissions/tree', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557516361732, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557516361733, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 't', '角色管理', 'GET', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557520556034, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557520556035, '创建角色', 'auth:role:add', NULL, NULL, NULL, 't', '角色管理', 'POST', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557520556036, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 't', '角色管理', 'PUT', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557524750338, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 't', '角色管理', 'DELETE', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557524750339, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/update/status', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557528944642, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 't', '用户管理', 'DELETE', '/cloud-auth/user/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557528944643, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/admin/create', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557528944644, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/list', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557533138945, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 't', '用户管理', 'POST', '/cloud-auth/user/search', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066511557533138946, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 't', '用户管理', 'GET', '/cloud-auth/user/details/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643354624002, '获取当前用户权限码', 'isAuthenticated()', NULL, NULL, NULL, 'f', '认证管理', 'GET', '/cloud-auth/auth/codes', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643363012610, '根据用户ID获取权限码', 'auth:user:query', NULL, NULL, NULL, 'f', '认证管理', 'GET', '/cloud-auth/auth/codes/user', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643367206913, '添加黑名单', 'blog:black:add', NULL, NULL, NULL, 'f', 'BlackListController', 'POST', '/cloud-auth/blackList/add', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643371401218, '修改黑名单', 'blog:black:update', NULL, NULL, NULL, 'f', 'BlackListController', 'PUT', '/cloud-auth/blackList/update', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643371401219, '删除黑名单', 'blog:black:delete', NULL, NULL, NULL, 'f', 'BlackListController', 'DELETE', '/cloud-auth/blackList/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643375595521, '查询黑名单', 'blog:black:select', NULL, NULL, NULL, 'f', 'BlackListController', 'POST', '/cloud-auth/blackList/getBlackListing', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643375595522, '获取菜单详情', 'auth:menu:list', NULL, NULL, NULL, 'f', '菜单管理', 'GET', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643379789826, '创建菜单', 'auth:menu:add', NULL, NULL, NULL, 'f', '菜单管理', 'POST', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643383984130, '更新菜单', 'auth:menu:edit', NULL, NULL, NULL, 'f', '菜单管理', 'PUT', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643383984131, '删除菜单', 'auth:menu:delete', NULL, NULL, NULL, 'f', '菜单管理', 'DELETE', '/cloud-auth/menu/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643388178434, '获取菜单列表（平铺）', 'auth:menu:list', NULL, NULL, NULL, 'f', '菜单管理', 'GET', '/cloud-auth/menu', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643388178435, '获取所有权限', 'auth:permission:list', NULL, NULL, NULL, 'f', '权限管理', 'GET', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643392372737, '创建权限', 'auth:permission:add', NULL, NULL, NULL, 'f', '权限管理', 'POST', '/cloud-auth/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643392372738, '更新权限', 'auth:permission:edit', NULL, NULL, NULL, 'f', '权限管理', 'PUT', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643396567041, '删除权限', 'auth:permission:delete', NULL, NULL, NULL, 'f', '权限管理', 'DELETE', '/cloud-auth/permissions/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643396567042, '获取权限树', 'auth:permission:list', NULL, NULL, NULL, 'f', '权限管理', 'GET', '/cloud-auth/permissions/tree', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643400761346, '获取所有角色', 'auth:role:list', NULL, NULL, NULL, 'f', '角色管理', 'GET', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643400761347, '获取角色权限ID列表', 'auth:role:permission:query', NULL, NULL, NULL, 'f', '角色管理', 'GET', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643400761348, '分配权限给角色', 'auth:role:permission:edit', NULL, NULL, NULL, 'f', '角色管理', 'PUT', '/cloud-auth/roles/{roleId}/permissions', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643404955650, '创建角色', 'auth:role:add', NULL, NULL, NULL, 'f', '角色管理', 'POST', '/cloud-auth/roles', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643404955651, '更新角色', 'auth:role:edit', NULL, NULL, NULL, 'f', '角色管理', 'PUT', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643404955652, '删除角色', 'auth:role:delete', NULL, NULL, NULL, 'f', '角色管理', 'DELETE', '/cloud-auth/roles/{id}', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643404955653, '更新用户状态', 'auth:user:edit', NULL, NULL, NULL, 'f', '用户管理', 'POST', '/cloud-auth/user/update/status', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643409149953, '删除用户', 'auth:user:delete', NULL, NULL, NULL, 'f', '用户管理', 'DELETE', '/cloud-auth/user/delete', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643409149954, '管理员创建用户', 'auth:user:add', NULL, NULL, NULL, 'f', '用户管理', 'POST', '/cloud-auth/user/admin/create', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643413344257, '获取用户列表', 'auth:user:list', NULL, NULL, NULL, 'f', '用户管理', 'GET', '/cloud-auth/user/list', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643421732865, '搜索用户列表', 'auth:user:list', NULL, NULL, NULL, 'f', '用户管理', 'POST', '/cloud-auth/user/search', 'cloud-auth');
INSERT INTO "auth"."sys_permission" VALUES (2066512643421732866, '获取用户详细信息', 'auth:user:query', NULL, NULL, NULL, 'f', '用户管理', 'GET', '/cloud-auth/user/details/{id}', 'cloud-auth');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS "auth"."sys_role";
CREATE TABLE "auth"."sys_role" (
  "id" int8 NOT NULL,
  "role_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "role_key" varchar(10) COLLATE "pg_catalog"."default" NOT NULL,
  "status" int2 DEFAULT 0,
  "order_num" int8 NOT NULL,
  "remark" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "is_deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "auth"."sys_role"."id" IS '角色id';
COMMENT ON COLUMN "auth"."sys_role"."role_name" IS '角色名称';
COMMENT ON COLUMN "auth"."sys_role"."role_key" IS '角色字符';
COMMENT ON COLUMN "auth"."sys_role"."status" IS '状态（0：正常，1：停用）';
COMMENT ON COLUMN "auth"."sys_role"."order_num" IS '排序';
COMMENT ON COLUMN "auth"."sys_role"."remark" IS '备注';
COMMENT ON TABLE "auth"."sys_role" IS '角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO "auth"."sys_role" VALUES (1, '超级管理员', 'ADMIN', 0, 0, '最高管理者', '2026-05-16 10:29:52.829561', '2026-05-16 10:29:52.829561', 'f');
INSERT INTO "auth"."sys_role" VALUES (2, '测试角色', 'Test', 0, 1, '测试的用户，没有任何操作权限', '2026-05-16 10:29:52.829561', '2026-05-16 10:29:52.829561', 'f');
INSERT INTO "auth"."sys_role" VALUES (3, '普通用户', 'USER', 0, 3, '前台普通用户（前台用户默认角色）', '2026-05-16 10:29:52.829561', '2026-05-16 10:29:52.829561', 'f');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS "auth"."sys_role_menu";
CREATE TABLE "auth"."sys_role_menu" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "menu_id" int8 NOT NULL,
  "is_deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON TABLE "auth"."sys_role_menu" IS '角色菜单关联表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO "auth"."sys_role_menu" VALUES (1, 1, 1, 'f');
INSERT INTO "auth"."sys_role_menu" VALUES (2, 1, 2, 'f');
INSERT INTO "auth"."sys_role_menu" VALUES (3, 1, 3, 'f');
INSERT INTO "auth"."sys_role_menu" VALUES (4, 1, 21, 'f');
INSERT INTO "auth"."sys_role_menu" VALUES (5, 1, 23, 'f');
INSERT INTO "auth"."sys_role_menu" VALUES (6, 1, 24, 'f');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS "auth"."sys_role_permission";
CREATE TABLE "auth"."sys_role_permission" (
  "id" int8 NOT NULL,
  "role_id" int8 NOT NULL,
  "permission_id" int8 NOT NULL
)
;
COMMENT ON TABLE "auth"."sys_role_permission" IS '角色权限关联表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779245879298, 1, 2066078241705168899);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779245879299, 1, 2066512643371401218);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779245879300, 1, 2066078241709363202);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779245879301, 1, 2066512643371401219);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779245879302, 1, 2066078241709363201);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779250073601, 1, 2066512643375595521);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779250073602, 1, 2066078241705168898);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779250073603, 1, 2066512643367206913);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779250073604, 1, 2066078241734529025);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779250073605, 1, 2066512643392372737);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779250073606, 1, 2066078241738723330);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779250073607, 1, 2066512643396567041);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779250073608, 1, 2066078241738723329);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779250073609, 1, 2066512643392372738);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779250073610, 1, 2066078241730334722);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779250073611, 1, 2066512643388178435);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267905, 1, 2066512643396567042);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267906, 1, 2066078241768083459);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267907, 1, 2066512643409149953);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267908, 1, 2066078241763889154);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267909, 1, 2066512643421732865);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267910, 1, 2066078241763889155);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267911, 1, 2066512643404955653);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267912, 1, 2066078241776472066);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267913, 1, 2066512643409149954);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267914, 1, 2066078241759694851);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267915, 1, 2066512643413344257);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779254267916, 1, 2066078241768083458);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462209, 1, 2066512643421732866);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462210, 1, 2066078241713557505);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462211, 1, 2066512643379789826);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462212, 1, 2066078241721946114);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462213, 1, 2066512643383984131);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462214, 1, 2066078241717751809);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462215, 1, 2066512643383984130);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462216, 1, 2066078241721946115);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462217, 1, 2066512643388178434);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462218, 1, 2066078241726140417);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462219, 1, 2066512643375595522);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462220, 1, 2066078241759694850);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462221, 1, 2066078241759694849);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779258462222, 1, 2066512643400761348);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656513, 1, 2066078241747111938);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656514, 1, 2066512643404955650);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656515, 1, 2066078241755500545);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656516, 1, 2066512643404955652);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656517, 1, 2066078241751306242);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656518, 1, 2066512643404955651);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656519, 1, 2066078241742917634);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656520, 1, 2066512643400761346);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656521, 1, 2066512643400761347);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656522, 1, 2066078241705168897);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656523, 1, 2066512643363012610);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779262656524, 1, 2066078241692585986);
INSERT INTO "auth"."sys_role_permission" VALUES (2066512779266850818, 1, 2066512643354624002);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS "auth"."sys_user";
CREATE TABLE "auth"."sys_user" (
  "id" int8 NOT NULL,
  "nickname" varchar(50) COLLATE "pg_catalog"."default",
  "username" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "gender" int2 DEFAULT 0,
  "password" varchar(100) COLLATE "pg_catalog"."default",
  "avatar" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
  "intro" varchar(100) COLLATE "pg_catalog"."default",
  "email" varchar(50) COLLATE "pg_catalog"."default",
  "register_ip" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
  "register_type" int2 NOT NULL,
  "register_address" varchar(50) COLLATE "pg_catalog"."default" NOT NULL,
  "login_ip" varchar(100) COLLATE "pg_catalog"."default",
  "login_address" varchar(50) COLLATE "pg_catalog"."default",
  "login_type" int2,
  "login_time" timestamp(6) NOT NULL,
  "is_disable" bool NOT NULL DEFAULT false,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "is_deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "auth"."sys_user"."id" IS '用户ID（雪花算法）';
COMMENT ON COLUMN "auth"."sys_user"."nickname" IS '用户昵称';
COMMENT ON COLUMN "auth"."sys_user"."username" IS '用户名（唯一）';
COMMENT ON COLUMN "auth"."sys_user"."gender" IS '性别（0:未定义, 1:男, 2:女）';
COMMENT ON COLUMN "auth"."sys_user"."password" IS '密码（BCrypt加密）';
COMMENT ON COLUMN "auth"."sys_user"."avatar" IS '用户头像';
COMMENT ON COLUMN "auth"."sys_user"."intro" IS '个人简介';
COMMENT ON COLUMN "auth"."sys_user"."email" IS '用户邮箱';
COMMENT ON COLUMN "auth"."sys_user"."register_ip" IS '注册ip';
COMMENT ON COLUMN "auth"."sys_user"."register_type" IS '注册方式(0邮箱/姓名 1Gitee 2Github)';
COMMENT ON COLUMN "auth"."sys_user"."register_address" IS '注册地址';
COMMENT ON COLUMN "auth"."sys_user"."login_ip" IS '最近登录ip';
COMMENT ON COLUMN "auth"."sys_user"."login_address" IS '最近登录地址';
COMMENT ON COLUMN "auth"."sys_user"."login_type" IS '最近登录类型(0邮箱/姓名 1Gitee 2Github)';
COMMENT ON COLUMN "auth"."sys_user"."login_time" IS '用户最近登录时间';
COMMENT ON COLUMN "auth"."sys_user"."is_disable" IS '是否禁用';
COMMENT ON COLUMN "auth"."sys_user"."create_time" IS '用户创建时间';
COMMENT ON COLUMN "auth"."sys_user"."update_time" IS '用户更新时间';
COMMENT ON COLUMN "auth"."sys_user"."is_deleted" IS '是否删除';
COMMENT ON TABLE "auth"."sys_user" IS '用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO "auth"."sys_user" VALUES (1, '超级管理员', 'ADMIN', 0, '$2a$10$VyFtQ3T943p3NY5R0IxzIONjdqABmuCSGiHe5uV8d1ujLGYuS2KZe', '', '系统管理员', 'admin@example.com', '127.0.0.1', 0, 'localhost', NULL, NULL, NULL, '2026-05-16 10:29:52.831061', 'f', '2026-05-16 10:29:52.831061', '2026-05-16 10:29:52.831061', 'f');
INSERT INTO "auth"."sys_user" VALUES (2064388032101908481, 'vben', 'vben', 0, '$2a$10$kUlWYfUsSyiLwxwrXi6KYuiYmsi/9VA4IbbPVJF.W1.z5nRSrtj42', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', NULL, 'overhyyds@gmail.com', '127.0.0.1', 0, '未知', '10.255.255.254', NULL, NULL, '2026-06-15 21:26:34.096775', 'f', NULL, NULL, 'f');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS "auth"."sys_user_role";
CREATE TABLE "auth"."sys_user_role" (
  "id" int8 NOT NULL,
  "user_id" int8 NOT NULL,
  "role_id" int8 NOT NULL
)
;
COMMENT ON TABLE "auth"."sys_user_role" IS '用户角色关联表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO "auth"."sys_user_role" VALUES (1, 1, 1);
INSERT INTO "auth"."sys_user_role" VALUES (2064388032122880001, 2064388032101908481, 1);

-- ----------------------------
-- Table structure for t_black_list
-- ----------------------------
DROP TABLE IF EXISTS "auth"."t_black_list";
CREATE TABLE "auth"."t_black_list" (
  "id" int8 NOT NULL,
  "user_id" int8,
  "reason" varchar(500) COLLATE "pg_catalog"."default",
  "banned_time" timestamp(6),
  "expires_time" timestamp(6),
  "type" int2,
  "ip_info" json,
  "create_time" timestamp(6),
  "update_time" timestamp(6),
  "is_deleted" bool NOT NULL DEFAULT false
)
;
COMMENT ON COLUMN "auth"."t_black_list"."user_id" IS '用户ID';
COMMENT ON COLUMN "auth"."t_black_list"."reason" IS '封禁原因描述';
COMMENT ON COLUMN "auth"."t_black_list"."banned_time" IS '封禁开始时间';
COMMENT ON COLUMN "auth"."t_black_list"."expires_time" IS '封禁到期时间，null表示永久封禁';
COMMENT ON COLUMN "auth"."t_black_list"."type" IS '黑名单类型：1-用户 2-路人/攻击者';
COMMENT ON COLUMN "auth"."t_black_list"."ip_info" IS 'IP相关信息，type=2时必填';
COMMENT ON TABLE "auth"."t_black_list" IS '黑名单表';

-- ----------------------------
-- Records of t_black_list
-- ----------------------------

-- ----------------------------
-- Indexes structure for table sys_dict_data
-- ----------------------------
CREATE INDEX "idx_sys_dict_data_dict_type" ON "auth"."sys_dict_data" USING btree (
  "dict_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_dict_data
-- ----------------------------
ALTER TABLE "auth"."sys_dict_data" ADD CONSTRAINT "sys_dict_data_pkey" PRIMARY KEY ("dict_code");

-- ----------------------------
-- Indexes structure for table sys_dict_type
-- ----------------------------
CREATE INDEX "idx_sys_dict_type_dict_type" ON "auth"."sys_dict_type" USING btree (
  "dict_type" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Uniques structure for table sys_dict_type
-- ----------------------------
ALTER TABLE "auth"."sys_dict_type" ADD CONSTRAINT "sys_dict_type_dict_type_key" UNIQUE ("dict_type");

-- ----------------------------
-- Primary Key structure for table sys_dict_type
-- ----------------------------
ALTER TABLE "auth"."sys_dict_type" ADD CONSTRAINT "sys_dict_type_pkey" PRIMARY KEY ("dict_id");

-- ----------------------------
-- Indexes structure for table sys_log
-- ----------------------------
CREATE INDEX "idx_sys_log_user_name" ON "auth"."sys_log" USING btree (
  "user_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_log
-- ----------------------------
ALTER TABLE "auth"."sys_log" ADD CONSTRAINT "sys_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_login_log
-- ----------------------------
CREATE INDEX "idx_sys_login_log_user_name" ON "auth"."sys_login_log" USING btree (
  "user_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_login_log
-- ----------------------------
ALTER TABLE "auth"."sys_login_log" ADD CONSTRAINT "sys_login_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_menu
-- ----------------------------
CREATE INDEX "idx_sys_menu_parent_id" ON "auth"."sys_menu" USING btree (
  "parent_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_menu
-- ----------------------------
ALTER TABLE "auth"."sys_menu" ADD CONSTRAINT "sys_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_permission
-- ----------------------------
CREATE INDEX "idx_sys_permission_menu_id" ON "auth"."sys_permission" USING btree (
  "menu_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_permission
-- ----------------------------
ALTER TABLE "auth"."sys_permission" ADD CONSTRAINT "sys_permission_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_role
-- ----------------------------
CREATE INDEX "idx_sys_role_role_key" ON "auth"."sys_role" USING btree (
  "role_key" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Uniques structure for table sys_role
-- ----------------------------
ALTER TABLE "auth"."sys_role" ADD CONSTRAINT "sys_role_role_key_key" UNIQUE ("role_key");

-- ----------------------------
-- Primary Key structure for table sys_role
-- ----------------------------
ALTER TABLE "auth"."sys_role" ADD CONSTRAINT "sys_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table sys_role_menu
-- ----------------------------
ALTER TABLE "auth"."sys_role_menu" ADD CONSTRAINT "sys_role_menu_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_role_permission
-- ----------------------------
CREATE INDEX "idx_sys_role_permission_permission_id" ON "auth"."sys_role_permission" USING btree (
  "permission_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sys_role_permission_role_id" ON "auth"."sys_role_permission" USING btree (
  "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_role_permission
-- ----------------------------
ALTER TABLE "auth"."sys_role_permission" ADD CONSTRAINT "sys_role_permission_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_user
-- ----------------------------
CREATE INDEX "idx_sys_user_email" ON "auth"."sys_user" USING btree (
  "email" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sys_user_username" ON "auth"."sys_user" USING btree (
  "username" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
);

-- ----------------------------
-- Uniques structure for table sys_user
-- ----------------------------
ALTER TABLE "auth"."sys_user" ADD CONSTRAINT "sys_user_username_key" UNIQUE ("username");

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE "auth"."sys_user" ADD CONSTRAINT "sys_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table sys_user_role
-- ----------------------------
CREATE INDEX "idx_sys_user_role_role_id" ON "auth"."sys_user_role" USING btree (
  "role_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);
CREATE INDEX "idx_sys_user_role_user_id" ON "auth"."sys_user_role" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table sys_user_role
-- ----------------------------
ALTER TABLE "auth"."sys_user_role" ADD CONSTRAINT "sys_user_role_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Indexes structure for table t_black_list
-- ----------------------------
CREATE INDEX "idx_t_black_list_user_id" ON "auth"."t_black_list" USING btree (
  "user_id" "pg_catalog"."int8_ops" ASC NULLS LAST
);

-- ----------------------------
-- Primary Key structure for table t_black_list
-- ----------------------------
ALTER TABLE "auth"."t_black_list" ADD CONSTRAINT "t_black_list_pkey" PRIMARY KEY ("id");
