package com.watermelon.authorization.user.core.mapper.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.watermelon.mybatisplus.hadler.JsonTypeHandler;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;


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

    @TableField(typeHandler = JsonTypeHandler.class)
    private Set<String> clientAuthenticationMethods;

    @TableField(typeHandler = JsonTypeHandler.class)
    private Set<String> authorizationGrantTypes;

    @TableField(typeHandler = JsonTypeHandler.class)
    private Set<String> redirectUris;

    @TableField(typeHandler = JsonTypeHandler.class)
    private Set<String> postLogoutRedirectUris;

    @TableField(typeHandler = JsonTypeHandler.class)
    private Set<String> scopes;

    @TableField(typeHandler = JsonTypeHandler.class)
    private Map<String, Object>  clientSettings;

    @TableField(typeHandler = JsonTypeHandler.class)
    private Map<String, Object> tokenSettings;
}
