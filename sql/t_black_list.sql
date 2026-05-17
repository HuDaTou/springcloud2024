-- 黑名单表 - PostgreSQL 语法
DROP TABLE IF EXISTS auth.t_black_list;
CREATE TABLE auth.t_black_list (
  id bigint NOT NULL COMMENT '黑名单记录ID',
  user_id bigint DEFAULT NULL COMMENT '用户ID',
  reason varchar(500) DEFAULT NULL COMMENT '封禁原因描述',
  banned_time timestamp DEFAULT NULL COMMENT '封禁开始时间',
  expires_time timestamp DEFAULT NULL COMMENT '封禁到期时间，null表示永久封禁',
  type integer DEFAULT NULL COMMENT '黑名单类型：1-用户 2-路人/攻击者',
  ip_info json DEFAULT NULL COMMENT 'IP相关信息，type=2时必填',
  create_time timestamp DEFAULT NULL COMMENT '创建时间',
  update_time timestamp DEFAULT NULL COMMENT '更新时间',
  is_deleted boolean NOT NULL DEFAULT false COMMENT '是否删除（false：未删除，true：已删除）',
  PRIMARY KEY (id)
);

COMMENT ON TABLE auth.t_black_list IS '黑名单表';
