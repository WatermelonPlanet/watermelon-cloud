package com.watermelon.authorization.user.core.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.watermelon.common.core.enums.AccountPlatform;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 第三方服务账号详情dto
 * @author byh
 * @date 2023-09-14
 * @description
 */
@Data
public class SysThirdUserDetailDto {

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
