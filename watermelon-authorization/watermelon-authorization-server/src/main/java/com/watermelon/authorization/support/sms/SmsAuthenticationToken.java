package com.watermelon.authorization.support.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author byh
 * @date 2023-09-18
 * @description
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 认证类型
     */
    private AuthorizationGrantType authorizationGrantType;
    /**
     * 用户名
     */
    private Authentication clientPrincipal;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 短信验证码
     */
    private String code;
    /**
     * scopes
     */
    private Set<String> scopes;
    /**
     * 扩展的参数
     */
    private Map<String, Object> additionalParameters;

    public SmsAuthenticationToken(
            AuthorizationGrantType authorizationGrantType,
            Authentication clientPrincipal,
            Set<String> scopes,
            String phone,
            String code,
            Map<String, Object> additionalParameters) {
        super(Collections.emptyList());

        Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
        this.scopes = Collections.unmodifiableSet(
                scopes != null ?
                        new HashSet<>(scopes) :
                        Collections.emptySet());

        this.phone = phone;
        this.code = code;
        this.clientPrincipal = clientPrincipal;
        this.additionalParameters = Collections.unmodifiableMap(
                additionalParameters != null ?
                        new HashMap<>(additionalParameters) :
                        Collections.emptyMap());
        this.authorizationGrantType = authorizationGrantType;
    }

    /**
     * 扩展模式一般不需要密码
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 获取用户名
     */
    @Override
    public Object getPrincipal() {
        return this.clientPrincipal;
    }


    public String getPhone() {
        return phone;
    }


    public String getCode() {
        return code;
    }

    /**
     * 获取请求的scopes
     */
    public Set<String> getScopes() {
        return this.scopes;
    }

    /**
     * 获取请求中的 grant_type
     */
    public AuthorizationGrantType getAuthorizationGrantType() {
        return this.authorizationGrantType;
    }

    /**
     * 获取请求中的附加参数
     */
    public Map<String, Object> getAdditionalParameters() {
        return this.additionalParameters;
    }

}
