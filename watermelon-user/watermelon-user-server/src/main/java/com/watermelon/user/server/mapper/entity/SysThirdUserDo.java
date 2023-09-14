package com.watermelon.user.server.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.watermelon.common.core.enums.AccountPlatform;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 第三方用户表
 *
 * @author byh
 * @date 2023-09-14
 * @description
 */
@Data
@TableName("sys_third_user")
public class SysThirdUserDo implements Serializable {
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedTime;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 第三方平台唯一id
     */
    private String uniqueId;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 平台类型(WX:微信；QQ:QQ)
     */
    private AccountPlatform type;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 用户id
     */
    private Long userId;

}
