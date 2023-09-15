package com.watermelon.tenant.user.api.dto;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

/**
 * (Oauth2RegisteredClient)实体类
 *
 * @author byh
 * @date 2023-09-14
 */
@Data
public class SysRegisteredClientDto implements Serializable {
    private String id;
    private String clientId;
    private LocalDateTime clientIdIssuedAt;
    private String clientSecret;
    private LocalDateTime clientSecretExpiresAt;
    private String clientName;
    private Set<String>  clientAuthenticationMethods;
    private Set<String>  authorizationGrantTypes;
    private Set<String> redirectUris;
    private Set<String> postLogoutRedirectUris;
    private Set<String> scopes;
    private Map<String, Object> clientSettings;
    private Map<String, Object> tokenSettings;

}
