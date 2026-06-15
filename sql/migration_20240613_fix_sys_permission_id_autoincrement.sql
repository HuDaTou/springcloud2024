-- =============================================
-- 数据库迁移脚本 - 2024-06-13
-- 修复 sys_permission 表的 id 自增列问题
-- =============================================

-- 1. 创建序列
CREATE SEQUENCE IF NOT EXISTS auth.sys_permission_id_seq;

-- 2. 将现有 id 的最大值设置为序列起始值
SELECT setval('auth.sys_permission_id_seq', COALESCE((SELECT MAX(id) FROM auth.sys_permission), 0) + 1);

-- 3. 修改 id 列，使用序列作为默认值
ALTER TABLE auth.sys_permission 
ALTER COLUMN id SET DEFAULT nextval('auth.sys_permission_id_seq');

-- 4. 设置序列归属
ALTER SEQUENCE auth.sys_permission_id_seq OWNED BY auth.sys_permission.id;
