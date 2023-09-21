
DROP TABLE IF EXISTS sys_third_user;
CREATE TABLE sys_third_user (
  create_time timestamp(6) NOT NULL DEFAULT timezone('UTC-8'::text, (now())::timestamp(0) without time zone),
  modified_time timestamp(6) DEFAULT timezone('UTC-8'::text, (now())::timestamp(0) without time zone),
  id int8 NOT NULL DEFAULT nextval('sys_third_user_id_seq'::regclass),
  unique_id varchar(128)  NOT NULL,
  name varchar(64)  NOT NULL,
  type varchar(64)  NOT NULL,
  avatar varchar(255)  DEFAULT NULL,
  user_id int8 NOT NULL
)
;
COMMENT ON COLUMN sys_third_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_third_user.modified_time IS '修改时间';
COMMENT ON COLUMN sys_third_user.id IS 'id';
COMMENT ON COLUMN sys_third_user.unique_id IS '第三方平台唯一id';
COMMENT ON COLUMN sys_third_user.name IS '用户名称';
COMMENT ON COLUMN sys_third_user.type IS '平台类型(WX:微信；QQ:QQ)';
COMMENT ON COLUMN sys_third_user.avatar IS '头像';
COMMENT ON COLUMN sys_third_user.user_id IS '用户id';
COMMENT ON TABLE sys_third_user IS '第三方用户表';

-- ----------------------------
-- Primary Key structure for table sys_third_user
-- ----------------------------
ALTER TABLE sys_third_user ADD CONSTRAINT sys_third_user_pkey PRIMARY KEY (id);
