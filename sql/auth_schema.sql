-- =============================================
-- Cloud-Auth 认证服务数据库初始化脚本
-- PostgreSQL 语法
-- =============================================

-- 创建 schema（如果不存在）
CREATE SCHEMA IF NOT EXISTS auth;

-- =============================================
-- 1. sys_user - 用户表
-- =============================================
DROP TABLE IF EXISTS auth.sys_user CASCADE;
CREATE TABLE auth.sys_user (
  id bigint NOT NULL,
  nickname varchar(50),
  username varchar(50) NOT NULL,
  gender smallint DEFAULT 0,
  password varchar(100),
  avatar varchar(255) NOT NULL,
  intro varchar(100),
  email varchar(50),
  register_ip varchar(100) NOT NULL,
  register_type smallint NOT NULL,
  register_address varchar(50) NOT NULL,
  login_ip varchar(100),
  login_address varchar(50),
  login_type smallint,
  login_time timestamp NOT NULL,
  is_disable boolean NOT NULL DEFAULT false,
  create_time timestamp,
  update_time timestamp,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id),
  UNIQUE (username)
);
COMMENT ON TABLE auth.sys_user IS '用户表';
COMMENT ON COLUMN auth.sys_user.id IS '用户ID（雪花算法）';
COMMENT ON COLUMN auth.sys_user.nickname IS '用户昵称';
COMMENT ON COLUMN auth.sys_user.username IS '用户名（唯一）';
COMMENT ON COLUMN auth.sys_user.gender IS '性别（0:未定义, 1:男, 2:女）';
COMMENT ON COLUMN auth.sys_user.password IS '密码（BCrypt加密）';
COMMENT ON COLUMN auth.sys_user.avatar IS '用户头像';
COMMENT ON COLUMN auth.sys_user.intro IS '个人简介';
COMMENT ON COLUMN auth.sys_user.email IS '用户邮箱';
COMMENT ON COLUMN auth.sys_user.register_ip IS '注册ip';
COMMENT ON COLUMN auth.sys_user.register_type IS '注册方式(0邮箱/姓名 1Gitee 2Github)';
COMMENT ON COLUMN auth.sys_user.register_address IS '注册地址';
COMMENT ON COLUMN auth.sys_user.login_ip IS '最近登录ip';
COMMENT ON COLUMN auth.sys_user.login_address IS '最近登录地址';
COMMENT ON COLUMN auth.sys_user.login_type IS '最近登录类型(0邮箱/姓名 1Gitee 2Github)';
COMMENT ON COLUMN auth.sys_user.login_time IS '用户最近登录时间';
COMMENT ON COLUMN auth.sys_user.is_disable IS '是否禁用';
COMMENT ON COLUMN auth.sys_user.create_time IS '用户创建时间';
COMMENT ON COLUMN auth.sys_user.update_time IS '用户更新时间';
COMMENT ON COLUMN auth.sys_user.is_deleted IS '是否删除';

-- =============================================
-- 2. sys_role - 角色表
-- =============================================
DROP TABLE IF EXISTS auth.sys_role CASCADE;
CREATE TABLE auth.sys_role (
  id bigint NOT NULL,
  role_name varchar(100) NOT NULL,
  role_key varchar(10) NOT NULL,
  status smallint DEFAULT 0,
  order_num bigint NOT NULL,
  remark varchar(255),
  create_time timestamp,
  update_time timestamp,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id),
  UNIQUE (role_key)
);
COMMENT ON TABLE auth.sys_role IS '角色表';
COMMENT ON COLUMN auth.sys_role.id IS '角色id';
COMMENT ON COLUMN auth.sys_role.role_name IS '角色名称';
COMMENT ON COLUMN auth.sys_role.role_key IS '角色字符';
COMMENT ON COLUMN auth.sys_role.status IS '状态（0：正常，1：停用）';
COMMENT ON COLUMN auth.sys_role.order_num IS '排序';
COMMENT ON COLUMN auth.sys_role.remark IS '备注';

-- =============================================
-- 3. sys_permission - 权限表
-- =============================================
DROP TABLE IF EXISTS auth.sys_permission CASCADE;
CREATE TABLE auth.sys_permission (
  id bigint NOT NULL,
  permission_desc varchar(50) NOT NULL,
  permission_key varchar(255) NOT NULL,
  menu_id bigint NOT NULL,
  create_time timestamp,
  update_time timestamp,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);
COMMENT ON TABLE auth.sys_permission IS '权限表';
COMMENT ON COLUMN auth.sys_permission.id IS '权限表id';
COMMENT ON COLUMN auth.sys_permission.permission_desc IS '描述';
COMMENT ON COLUMN auth.sys_permission.permission_key IS '权限字符';
COMMENT ON COLUMN auth.sys_permission.menu_id IS '菜单id';

-- =============================================
-- 4. sys_user_role - 用户角色关联表
-- =============================================
DROP TABLE IF EXISTS auth.sys_user_role CASCADE;
CREATE TABLE auth.sys_user_role (
  id bigint NOT NULL,
  user_id bigint NOT NULL,
  role_id bigint NOT NULL,
  PRIMARY KEY (id)
);
COMMENT ON TABLE auth.sys_user_role IS '用户角色关联表';

-- =============================================
-- 5. sys_role_permission - 角色权限关联表
-- =============================================
DROP TABLE IF EXISTS auth.sys_role_permission CASCADE;
CREATE TABLE auth.sys_role_permission (
  id bigint NOT NULL,
  role_id bigint NOT NULL,
  permission_id bigint NOT NULL,
  PRIMARY KEY (id)
);
COMMENT ON TABLE auth.sys_role_permission IS '角色权限关联表';

-- =============================================
-- 6. sys_menu - 菜单表
-- =============================================
DROP TABLE IF EXISTS auth.sys_menu CASCADE;
CREATE TABLE auth.sys_menu (
  id bigint NOT NULL,
  title varchar(50) NOT NULL,
  icon varchar(50),
  path varchar(255) NOT NULL,
  component varchar(255),
  redirect varchar(255),
  affix smallint DEFAULT 0,
  parent_id bigint,
  name varchar(255),
  hide_in_menu smallint DEFAULT 0,
  url varchar(255),
  hide_in_breadcrumb smallint DEFAULT 1,
  hide_children_in_menu smallint DEFAULT 1,
  keep_alive smallint DEFAULT 1,
  target varchar(255),
  is_disable boolean NOT NULL DEFAULT false,
  order_num int NOT NULL DEFAULT 1,
  create_time timestamp,
  update_time timestamp,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);
COMMENT ON TABLE auth.sys_menu IS '菜单表';

-- =============================================
-- 7. t_black_list - 黑名单表
-- =============================================
DROP TABLE IF EXISTS auth.t_black_list CASCADE;
CREATE TABLE auth.t_black_list (
  id bigint NOT NULL,
  user_id bigint,
  reason varchar(500),
  banned_time timestamp,
  expires_time timestamp,
  type smallint,
  ip_info json,
  create_time timestamp,
  update_time timestamp,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);
COMMENT ON TABLE auth.t_black_list IS '黑名单表';
COMMENT ON COLUMN auth.t_black_list.user_id IS '用户ID';
COMMENT ON COLUMN auth.t_black_list.reason IS '封禁原因描述';
COMMENT ON COLUMN auth.t_black_list.banned_time IS '封禁开始时间';
COMMENT ON COLUMN auth.t_black_list.expires_time IS '封禁到期时间，null表示永久封禁';
COMMENT ON COLUMN auth.t_black_list.type IS '黑名单类型：1-用户 2-路人/攻击者';
COMMENT ON COLUMN auth.t_black_list.ip_info IS 'IP相关信息，type=2时必填';

-- =============================================
-- 8. sys_login_log - 登录日志表
-- =============================================
DROP TABLE IF EXISTS auth.sys_login_log CASCADE;
CREATE TABLE auth.sys_login_log (
  id bigint NOT NULL,
  user_name varchar(50) NOT NULL,
  ip varchar(50) NOT NULL,
  address varchar(50) NOT NULL,
  browser varchar(50) NOT NULL,
  os varchar(50) NOT NULL,
  type smallint NOT NULL,
  state smallint NOT NULL,
  message text NOT NULL,
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);
COMMENT ON TABLE auth.sys_login_log IS '登录日志表';

-- =============================================
-- 9. sys_log - 操作日志表
-- =============================================
DROP TABLE IF EXISTS auth.sys_log CASCADE;
CREATE TABLE auth.sys_log (
  id bigint NOT NULL,
  module varchar(50) NOT NULL,
  operation varchar(50) NOT NULL,
  user_name varchar(50) NOT NULL,
  ip varchar(100) NOT NULL,
  address varchar(50) NOT NULL,
  state smallint NOT NULL,
  method varchar(255) NOT NULL,
  req_parameter text,
  req_mapping varchar(10) NOT NULL,
  exception text,
  return_parameter text,
  req_address varchar(255) NOT NULL,
  time bigint NOT NULL,
  description varchar(100),
  create_time timestamp NOT NULL,
  update_time timestamp NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);
COMMENT ON TABLE auth.sys_log IS '操作日志表';

-- =============================================
-- 10. sys_dict_type - 字典类型表
-- =============================================
DROP TABLE IF EXISTS auth.sys_dict_type CASCADE;
CREATE TABLE auth.sys_dict_type (
  dict_id bigint NOT NULL,
  dict_name varchar(100) DEFAULT '',
  dict_type varchar(100) DEFAULT '',
  status char(1) DEFAULT '0',
  create_by varchar(64) DEFAULT '',
  create_time timestamp,
  update_by varchar(64) DEFAULT '',
  update_time timestamp,
  remark varchar(500),
  PRIMARY KEY (dict_id),
  UNIQUE (dict_type)
);
COMMENT ON TABLE auth.sys_dict_type IS '字典类型表';

-- =============================================
-- 11. sys_dict_data - 字典数据表
-- =============================================
DROP TABLE IF EXISTS auth.sys_dict_data CASCADE;
CREATE TABLE auth.sys_dict_data (
  dict_code bigint NOT NULL,
  dict_sort int DEFAULT 0,
  dict_label varchar(100) DEFAULT '',
  dict_value varchar(100) DEFAULT '',
  dict_type varchar(100) DEFAULT '',
  css_class varchar(100),
  list_class varchar(100),
  is_default char(1) DEFAULT 'N',
  status char(1) DEFAULT '0',
  create_by varchar(64) DEFAULT '',
  create_time timestamp,
  update_by varchar(64) DEFAULT '',
  update_time timestamp,
  remark varchar(500),
  PRIMARY KEY (dict_code)
);
COMMENT ON TABLE auth.sys_dict_data IS '字典数据表';

-- =============================================
-- 12. sys_role_menu - 角色菜单关联表
-- =============================================
DROP TABLE IF EXISTS auth.sys_role_menu CASCADE;
CREATE TABLE auth.sys_role_menu (
  id bigint NOT NULL,
  role_id bigint NOT NULL,
  menu_id bigint NOT NULL,
  is_deleted boolean NOT NULL DEFAULT false,
  PRIMARY KEY (id)
);
COMMENT ON TABLE auth.sys_role_menu IS '角色菜单关联表';

-- =============================================
-- 插入初始数据
-- =============================================

-- 插入默认角色
INSERT INTO auth.sys_role (id, role_name, role_key, status, order_num, remark, create_time, update_time, is_deleted) VALUES
(1, '超级管理员', 'ADMIN', 0, 0, '最高管理者', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(2, '测试角色', 'Test', 0, 1, '测试的用户，没有任何操作权限', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(3, '普通用户', 'USER', 0, 3, '前台普通用户（前台用户默认角色）', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 插入默认管理员用户（密码: 123456 BCrypt加密）
INSERT INTO auth.sys_user (id, nickname, username, gender, password, avatar, intro, email, register_ip, register_type, register_address, login_time, is_disable, create_time, update_time, is_deleted) VALUES
(1, '超级管理员', 'ADMIN', 0, '$2a$10$VyFtQ3T943p3NY5R0IxzIONjdqABmuCSGiHe5uV8d1ujLGYuS2KZe', '', '系统管理员', 'admin@example.com', '127.0.0.1', 0, 'localhost', CURRENT_TIMESTAMP, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 关联管理员角色
INSERT INTO auth.sys_user_role (id, user_id, role_id) VALUES
(1, 1, 1);

-- 插入字典类型
INSERT INTO auth.sys_dict_type (dict_id, dict_name, dict_type, status, create_by, create_time) VALUES
(1, '用户性别', 'sys_user_sex', '0', 'admin', CURRENT_TIMESTAMP),
(2, '菜单状态', 'sys_show_hide', '0', 'admin', CURRENT_TIMESTAMP),
(3, '系统开关', 'sys_normal_disable', '0', 'admin', CURRENT_TIMESTAMP),
(4, '系统是否', 'sys_yes_no', '0', 'admin', CURRENT_TIMESTAMP);

-- 插入字典数据
INSERT INTO auth.sys_dict_data (dict_code, dict_sort, dict_label, dict_value, dict_type, is_default, status, create_by, create_time) VALUES
(1, 1, '男', '0', 'sys_user_sex', 'Y', '0', 'admin', CURRENT_TIMESTAMP),
(2, 2, '女', '1', 'sys_user_sex', 'N', '0', 'admin', CURRENT_TIMESTAMP),
(3, 3, '未知', '2', 'sys_user_sex', 'N', '0', 'admin', CURRENT_TIMESTAMP),
(4, 1, '显示', '0', 'sys_show_hide', 'Y', '0', 'admin', CURRENT_TIMESTAMP),
(5, 2, '隐藏', '1', 'sys_show_hide', 'N', '0', 'admin', CURRENT_TIMESTAMP),
(6, 1, '正常', '0', 'sys_normal_disable', 'Y', '0', 'admin', CURRENT_TIMESTAMP),
(7, 2, '停用', '1', 'sys_normal_disable', 'N', '0', 'admin', CURRENT_TIMESTAMP),
(8, 1, '是', 'Y', 'sys_yes_no', 'Y', '0', 'admin', CURRENT_TIMESTAMP),
(9, 2, '否', 'N', 'sys_yes_no', 'N', '0', 'admin', CURRENT_TIMESTAMP);

-- 插入默认菜单
INSERT INTO auth.sys_menu (id, title, icon, path, component, redirect, affix, parent_id, order_num, is_disable, create_time, update_time, is_deleted) VALUES
(1, '系统管理', 'SettingTwoTone', '/system', 'RouteView', '/system/menu', 0, NULL, 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(2, '菜单管理', 'MenuOutlined', '/system/menu', '/system/menu', '', 0, 1, 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(3, '用户管理', 'UserOutlined', '/system/user', '/system/user', '', 0, 1, 0, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(21, '首页', 'HomeTwoTone', '/welcome', '/welcome', '', 0, NULL, 0, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(23, '角色管理', 'TeamOutlined', '/system/role', '/system/role', NULL, 0, 1, 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(24, '权限管理', 'UnlockOutlined', '/system/permission', '/system/permission', '', 0, 1, 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 插入默认权限
INSERT INTO auth.sys_permission (id, permission_desc, permission_key, menu_id, create_time, update_time, is_deleted) VALUES
(1, '获取菜单', 'system:menu:list', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(2, '查询菜单', 'system:menu:select', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(3, '修改菜单', 'system:menu:update', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(4, '删除菜单', 'system:menu:delete', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(5, '添加菜单', 'system:menu:add', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(6, '获取所有角色', 'system:role:list', 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(7, '获取对应角色信息', 'system:role:get', 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(8, '修改角色信息', 'system:role:update', 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(9, '根据id删除角色', 'system:role:delete', 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(10, '获取用户列表', 'system:user:list', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(11, '获取用户详细信息', 'system:user:details', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false),
(12, '删除用户', 'system:user:delete', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, false);

-- 关联角色权限
INSERT INTO auth.sys_role_permission (id, role_id, permission_id) VALUES
(1, 1, 1), (2, 1, 2), (3, 1, 3), (4, 1, 4), (5, 1, 5),
(6, 1, 6), (7, 1, 7), (8, 1, 8), (9, 1, 9),
(10, 1, 10), (11, 1, 11), (12, 1, 12);

-- 关联角色菜单
INSERT INTO auth.sys_role_menu (id, role_id, menu_id, is_deleted) VALUES
(1, 1, 1, false), (2, 1, 2, false), (3, 1, 3, false),
(4, 1, 21, false), (5, 1, 23, false), (6, 1, 24, false);

-- =============================================
-- 创建索引
-- =============================================
CREATE INDEX IF NOT EXISTS idx_sys_user_username ON auth.sys_user(username);
CREATE INDEX IF NOT EXISTS idx_sys_user_email ON auth.sys_user(email);
CREATE INDEX IF NOT EXISTS idx_sys_role_role_key ON auth.sys_role(role_key);
CREATE INDEX IF NOT EXISTS idx_sys_permission_menu_id ON auth.sys_permission(menu_id);
CREATE INDEX IF NOT EXISTS idx_sys_user_role_user_id ON auth.sys_user_role(user_id);
CREATE INDEX IF NOT EXISTS idx_sys_user_role_role_id ON auth.sys_user_role(role_id);
CREATE INDEX IF NOT EXISTS idx_sys_role_permission_role_id ON auth.sys_role_permission(role_id);
CREATE INDEX IF NOT EXISTS idx_sys_role_permission_permission_id ON auth.sys_role_permission(permission_id);
CREATE INDEX IF NOT EXISTS idx_sys_menu_parent_id ON auth.sys_menu(parent_id);
CREATE INDEX IF NOT EXISTS idx_t_black_list_user_id ON auth.t_black_list(user_id);
CREATE INDEX IF NOT EXISTS idx_sys_login_log_user_name ON auth.sys_login_log(user_name);
CREATE INDEX IF NOT EXISTS idx_sys_log_user_name ON auth.sys_log(user_name);
CREATE INDEX IF NOT EXISTS idx_sys_dict_type_dict_type ON auth.sys_dict_type(dict_type);
CREATE INDEX IF NOT EXISTS idx_sys_dict_data_dict_type ON auth.sys_dict_data(dict_type);

-- =============================================
-- 脚本执行完成
-- =============================================
SELECT 'Auth schema initialized successfully!' AS result;
