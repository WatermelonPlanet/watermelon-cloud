
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
  create_time timestamp(6) NOT NULL DEFAULT timezone('UTC-8'::text, (now())::timestamp(0) without time zone),
  modified_time timestamp(6) DEFAULT timezone('UTC-8'::text, (now())::timestamp(0) without time zone),
  id int8 NOT NULL DEFAULT nextval('sys_user_id_seq'::regclass),
  name varchar(64)  NOT NULL,
  password varchar(255) ,
  phone varchar(11)  NOT NULL,
  mobile varchar(255)  NOT NULL,
  avatar varchar(255) ,
  status int2 NOT NULL DEFAULT 1
)
;
COMMENT ON COLUMN sys_user.create_time IS '创建时间';
COMMENT ON COLUMN sys_user.modified_time IS '修改时间';
COMMENT ON COLUMN sys_user.id IS 'id';
COMMENT ON COLUMN sys_user.name IS '用户名称';
COMMENT ON COLUMN sys_user.password IS '密码';
COMMENT ON COLUMN sys_user.phone IS '手机号(未加密)';
COMMENT ON COLUMN sys_user.mobile IS '手机号(加密)';
COMMENT ON COLUMN sys_user.avatar IS '头像';
COMMENT ON COLUMN sys_user.status IS '账号状态(0:无效；1:有效)';
COMMENT ON TABLE sys_user IS '用户表';

-- ----------------------------
-- Primary Key structure for table sys_user
-- ----------------------------
ALTER TABLE sys_user ADD CONSTRAINT sys_user_pkey PRIMARY KEY (id);
