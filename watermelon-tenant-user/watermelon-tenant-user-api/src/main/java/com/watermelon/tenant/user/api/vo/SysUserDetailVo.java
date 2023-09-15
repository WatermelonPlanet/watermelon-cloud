package com.watermelon.tenant.user.api.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户
 * @author byh
 * @date 2023-09-14
 * @description
 */
@Data
public class SysUserDetailVo implements Serializable {
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
    private Long id;
    /**
     * 用户名称
     */
    private String name;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号(未加密)
     */
    private String phone;
    /**
     * 手机号(加密)
     */
    private String mobile;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 账号状态(0:无效；1:有效)
     */
    private Integer status;
}