package com.watermelon.authorization.oauth2.tokenGenerator;

import cn.hutool.core.lang.Snowflake;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.time.Instant;
import java.util.Collections;

/**
 * 有时间可以搞一搞
 * @author byh
 * @description
 */
public class AesEncryptionOAuth2TokenGenerator implements OAuth2TokenGenerator<AesEncryptionOAuth2AccessToken> {

    private AecEncryptionStringKeyGenerator aecEncryptionStringKeyGenerator = new AecEncryptionStringKeyGenerator();

    @Override
    public AesEncryptionOAuth2AccessToken generate(OAuth2TokenContext context) {
//        if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) ||!OAuth2TokenType.REFRESH_TOKEN.equals(context.getTokenType())) {
//            return null;
//        }

        String issuer = null;
        if (context.getAuthorizationServerContext() != null) {
            issuer = context.getAuthorizationServerContext().getIssuer();
        }
        RegisteredClient registeredClient = context.getRegisteredClient();

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive());

        // @formatter:off
        OAuth2TokenClaimsSet.Builder claimsBuilder = OAuth2TokenClaimsSet.builder();
        if (StringUtils.hasText(issuer)) {
            claimsBuilder.issuer(issuer);
        }
        String idStr = new Snowflake().nextIdStr();
        claimsBuilder
                .subject(context.getPrincipal().getName())
                .audience(Collections.singletonList(registeredClient.getClientId()))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .notBefore(issuedAt)
                .id(idStr);
        if (!CollectionUtils.isEmpty(context.getAuthorizedScopes())) {
            claimsBuilder.claim(OAuth2ParameterNames.SCOPE, context.getAuthorizedScopes());
        }
        // @formatter:on
        OAuth2TokenClaimsSet accessTokenClaimsSet = claimsBuilder.build();
        aecEncryptionStringKeyGenerator=new AecEncryptionStringKeyGenerator(registeredClient.getClientSecret(), registeredClient.getClientId(), idStr);
        String tokenValue = aecEncryptionStringKeyGenerator.generateKey();
        return new AesEncryptionOAuth2AccessToken(idStr, AesEncryptionOAuth2AccessToken.AesEncryptionTokenType.BEARER, tokenValue, accessTokenClaimsSet.getIssuedAt(), accessTokenClaimsSet.getExpiresAt(),
                context.getAuthorizedScopes());
    }
}
