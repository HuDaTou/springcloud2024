-- =============================================
-- 数据库迁移脚本 - 2024-06-11
-- 为 sys_permission 表添加缺失的字段
-- =============================================

-- 添加 category 字段
ALTER TABLE auth.sys_permission ADD COLUMN IF NOT EXISTS category VARCHAR(255);
COMMENT ON COLUMN auth.sys_permission.category IS '权限类别';

-- 添加 http_method 字段
ALTER TABLE auth.sys_permission ADD COLUMN IF NOT EXISTS http_method VARCHAR(50);
COMMENT ON COLUMN auth.sys_permission.http_method IS 'HTTP请求方法';

-- 添加 path 字段
ALTER TABLE auth.sys_permission ADD COLUMN IF NOT EXISTS path VARCHAR(500);
COMMENT ON COLUMN auth.sys_permission.path IS '权限的完整请求路径';
