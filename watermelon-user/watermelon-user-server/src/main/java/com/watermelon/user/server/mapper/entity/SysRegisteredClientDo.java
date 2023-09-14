package com.watermelon.user.server.mapper.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;


/**
 * (Oauth2RegisteredClient)表实体类
 * @author byh
 * @since 2023-09-14
 */
@Data
@TableName(value = "sys_registered_client", autoResultMap = true)
public class SysRegisteredClientDo implements Serializable {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String clientId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clientIdIssuedAt;

    private String clientSecret;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clientSecretExpiresAt;

    private String clientName;

    private String clientAuthenticationMethods;

    private String authorizationGrantTypes;

    private String redirectUris;

    private String postLogoutRedirectUris;

    private String scopes;

    private String  clientSettings;

    private String tokenSettings;

}
