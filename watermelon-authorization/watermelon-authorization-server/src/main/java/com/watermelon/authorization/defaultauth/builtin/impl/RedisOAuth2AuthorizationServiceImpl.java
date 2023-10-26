package com.watermelon.authorization.defaultauth.builtin.impl;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2DeviceCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2UserCode;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author byh
 * @description
 */
@Component
public class RedisOAuth2AuthorizationServiceImpl implements OAuth2AuthorizationService {


    private final static String AUTHORIZATION_TYPE = "authorization_type";

    private final static String OAUTH2_PARAMETER_NAME_ID = "id";

    private final static Long TIMEOUT = 600L;


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(OAuth2Authorization authorization) {
        Assert.notNull(authorization, "authorization cannot be null");
        redisTemplate.setValueSerializer(RedisSerializer.java());
        redisTemplate.opsForValue().set(buildAuthorizationKey(OAUTH2_PARAMETER_NAME_ID, authorization.getId()), authorization, TIMEOUT, TimeUnit.SECONDS);
        if (isState(authorization)) {
            String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
            String isStateKey = buildAuthorizationKey(OAuth2ParameterNames.STATE, state);
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue().set(isStateKey, authorization, TIMEOUT, TimeUnit.SECONDS);
        }
        if (isAuthorizationCode(authorization)) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                    authorization.getToken(OAuth2AuthorizationCode.class);
            String tokenValue = authorizationCode.getToken().getTokenValue();
            String isAuthorizationCodeKey = buildAuthorizationKey(OAuth2ParameterNames.CODE, tokenValue);
            Instant expiresAt = authorizationCode.getToken().getExpiresAt();//过期时间
            Instant issuedAt = authorizationCode.getToken().getIssuedAt();//发放token的时间
            Date expiresAtDate = Date.from(expiresAt);
            Date issuedAtDate = Date.from(issuedAt);
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue().set(isAuthorizationCodeKey, authorization, TIMEOUT, TimeUnit.SECONDS);
        }
        if (isAccessToken(authorization)) {
            OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                    authorization.getToken(OAuth2AccessToken.class);
            String tokenValue = accessToken.getToken().getTokenValue();
            String isAccessTokenKey = buildAuthorizationKey(OAuth2ParameterNames.ACCESS_TOKEN, tokenValue);
            Instant expiresAt = accessToken.getToken().getExpiresAt();//过期时间
            Instant issuedAt = accessToken.getToken().getIssuedAt();//发放token的时间
            Date expiresAtDate = Date.from(expiresAt);
            Date issuedAtDate = Date.from(issuedAt);
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue().set(isAccessTokenKey, authorization, TIMEOUT, TimeUnit.SECONDS);
        }
        if (isRefreshToken(authorization)) {
            OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                    authorization.getToken(OAuth2RefreshToken.class);
            String tokenValue = refreshToken.getToken().getTokenValue();
            String isRefreshTokenKey = buildAuthorizationKey(OAuth2ParameterNames.REFRESH_TOKEN, tokenValue);
            Instant expiresAt = refreshToken.getToken().getExpiresAt();//过期时间
            Instant issuedAt = refreshToken.getToken().getIssuedAt();//发放token的时间
            Date expiresAtDate = Date.from(expiresAt);
            Date issuedAtDate = Date.from(issuedAt);
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue().set(isRefreshTokenKey, authorization, TIMEOUT, TimeUnit.SECONDS);

        }
        if (isIdToken(authorization)) {
            OAuth2Authorization.Token<OidcIdToken> idToken =
                    authorization.getToken(OidcIdToken.class);
            String tokenValue = idToken.getToken().getTokenValue();
            String isIdTokenKey = buildAuthorizationKey(OidcParameterNames.ID_TOKEN, tokenValue);
            Instant expiresAt = idToken.getToken().getExpiresAt();//过期时间
            Instant issuedAt = idToken.getToken().getIssuedAt();//发放token的时间
            Date expiresAtDate = Date.from(expiresAt);
            Date issuedAtDate = Date.from(issuedAt);
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue().set(isIdTokenKey, authorization, TIMEOUT, TimeUnit.SECONDS);
        }
        if (isDeviceCode(authorization)) {
            OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode =
                    authorization.getToken(OAuth2DeviceCode.class);

            String tokenValue = deviceCode.getToken().getTokenValue();
            String isDeviceCodeKey = buildAuthorizationKey(OAuth2ParameterNames.DEVICE_CODE, tokenValue);
            Instant expiresAt = deviceCode.getToken().getExpiresAt();//过期时间
            Instant issuedAt = deviceCode.getToken().getIssuedAt();//发放token的时间
            Date expiresAtDate = Date.from(expiresAt);
            Date issuedAtDate = Date.from(issuedAt);
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue().set(isDeviceCodeKey, authorization, TIMEOUT, TimeUnit.SECONDS);
        }
        if (isUserCode(authorization)) {
            OAuth2Authorization.Token<OAuth2UserCode> userCode =
                    authorization.getToken(OAuth2UserCode.class);
            String tokenValue = userCode.getToken().getTokenValue();
            String isUserCodeKey = buildAuthorizationKey(OAuth2ParameterNames.USER_CODE, tokenValue);
            Instant expiresAt = userCode.getToken().getExpiresAt();//过期时间
            Instant issuedAt = userCode.getToken().getIssuedAt();//发放token的时间
            Date expiresAtDate = Date.from(expiresAt);
            Date issuedAtDate = Date.from(issuedAt);
            redisTemplate.setValueSerializer(RedisSerializer.java());
            redisTemplate.opsForValue().set(isUserCodeKey, authorization, TIMEOUT, TimeUnit.SECONDS);
        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        List<String> keys = new ArrayList<>();
        String idKey = buildAuthorizationKey(OAUTH2_PARAMETER_NAME_ID, authorization.getId());
        keys.add(idKey);
        if (isState(authorization)) {
            String state = authorization.getAttribute(OAuth2ParameterNames.STATE);
            String isStateKey = buildAuthorizationKey(OAuth2ParameterNames.STATE, state);
            keys.add(isStateKey);
        }
        if (isAuthorizationCode(authorization)) {
            OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                    authorization.getToken(OAuth2AuthorizationCode.class);
            String tokenValue = authorizationCode.getToken().getTokenValue();
            String isAuthorizationCodeKey = buildAuthorizationKey(OAuth2ParameterNames.CODE, tokenValue);
            keys.add(isAuthorizationCodeKey);
        }
        if (isAccessToken(authorization)) {
            OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                    authorization.getToken(OAuth2AccessToken.class);
            String tokenValue = accessToken.getToken().getTokenValue();
            String isAccessTokenKey = buildAuthorizationKey(OAuth2ParameterNames.ACCESS_TOKEN, tokenValue);
            keys.add(isAccessTokenKey);
        }
        if (isRefreshToken(authorization)) {
            OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                    authorization.getToken(OAuth2RefreshToken.class);
            String tokenValue = refreshToken.getToken().getTokenValue();
            String isRefreshTokenKey = buildAuthorizationKey(OAuth2ParameterNames.REFRESH_TOKEN, tokenValue);
            keys.add(isRefreshTokenKey);
        }
        if (isIdToken(authorization)) {
            OAuth2Authorization.Token<OidcIdToken> idToken =
                    authorization.getToken(OidcIdToken.class);
            String tokenValue = idToken.getToken().getTokenValue();
            String isIdTokenKey = buildAuthorizationKey(OidcParameterNames.ID_TOKEN, tokenValue);
            keys.add(isIdTokenKey);
        }
        if (isDeviceCode(authorization)) {
            OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode =
                    authorization.getToken(OAuth2DeviceCode.class);

            String tokenValue = deviceCode.getToken().getTokenValue();
            String isDeviceCodeKey = buildAuthorizationKey(OAuth2ParameterNames.DEVICE_CODE, tokenValue);
            keys.add(isDeviceCodeKey);
        }
        if (isUserCode(authorization)) {
            OAuth2Authorization.Token<OAuth2UserCode> userCode =
                    authorization.getToken(OAuth2UserCode.class);
            String tokenValue = userCode.getToken().getTokenValue();
            String isUserCodeKey = buildAuthorizationKey(OAuth2ParameterNames.USER_CODE, tokenValue);
            keys.add(isUserCodeKey);
        }
        redisTemplate.delete(keys);
    }

    @Override
    public OAuth2Authorization findById(String id) {
        return (OAuth2Authorization) Optional.ofNullable(redisTemplate.opsForValue().get(buildAuthorizationKey(OAUTH2_PARAMETER_NAME_ID, id))).orElse(null);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        Assert.hasText(token, "token cannot be empty");
        Assert.notNull(tokenType, "tokenType cannot be empty");
        redisTemplate.setValueSerializer(RedisSerializer.java());
        return (OAuth2Authorization) redisTemplate.opsForValue().get(buildAuthorizationKey(tokenType.getValue(), token));
    }


    private boolean isState(OAuth2Authorization authorization) {
        return Objects.nonNull(authorization.getAttribute(OAuth2ParameterNames.STATE));
    }


    private boolean isAuthorizationCode(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode =
                authorization.getToken(OAuth2AuthorizationCode.class);
        return Objects.nonNull(authorizationCode);
    }


    private boolean isAccessToken(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2AccessToken> accessToken =
                authorization.getToken(OAuth2AccessToken.class);
        return Objects.nonNull(accessToken) && Objects.nonNull(accessToken.getToken().getTokenType());
    }

    private boolean isRefreshToken(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken =
                authorization.getToken(OAuth2RefreshToken.class);
        return Objects.nonNull(refreshToken) && Objects.nonNull(refreshToken.getToken().getTokenValue());
    }

    private boolean isIdToken(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OidcIdToken> idToken =
                authorization.getToken(OidcIdToken.class);
        return Objects.nonNull(idToken) && Objects.nonNull(idToken.getToken().getTokenValue());
    }

    private boolean isDeviceCode(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2DeviceCode> deviceCode =
                authorization.getToken(OAuth2DeviceCode.class);
        return Objects.nonNull(deviceCode) && Objects.nonNull(deviceCode.getToken().getTokenValue());
    }

    private boolean isUserCode(OAuth2Authorization authorization) {
        OAuth2Authorization.Token<OAuth2UserCode> userCode =
                authorization.getToken(OAuth2UserCode.class);
        return Objects.nonNull(userCode) && Objects.nonNull(userCode.getToken().getTokenValue());
    }


    /**
     * redis key 构建
     *
     * @param type  授权类型
     * @param value 授权值
     * @return
     */
    private String buildAuthorizationKey(String type, String value) {
        return AUTHORIZATION_TYPE.concat("::").concat(type).concat("::").concat(value);
    }
}
