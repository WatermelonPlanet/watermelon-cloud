DROP TABLE IF EXISTS `sys_third_user`;
CREATE TABLE `sys_third_user`
(
    `id`            bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `user_id`       bigint                                                        NOT NULL COMMENT '用户id',
    `name`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
    `platform`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '平台类型(WX:微信；QQ:QQ)',
    `avatar`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
    `unique_id`     varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '第三方平台唯一id',
    `create_time`   datetime                                                      NOT NULL COMMENT '创建时间',
    `modified_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT = '第三方用户表';

