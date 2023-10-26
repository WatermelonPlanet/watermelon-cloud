package com.watermelon.authorization.oauth2.tokenGenerator;

import cn.hutool.core.lang.Snowflake;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import java.time.Instant;
import java.util.Collections;

/**
 * token 自定义 Generator
 * @author byh
 * @description
 */
public class AesEncryptionOAuth2TokenGenerator implements OAuth2TokenGenerator<OAuth2AccessToken> {

    private AecEncryptionStringKeyGenerator aecEncryptionStringKeyGenerator = new AecEncryptionStringKeyGenerator();

    private OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer;


    @Override
    public OAuth2AccessToken generate(OAuth2TokenContext context) {
        if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            return null;
        }

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

        Authentication principal = context.getPrincipal();//这个地方实际上就是 provider 中的  DefaultOAuth2TokenContext.builder().principal(authentication) 中的 authentication
        claimsBuilder
                .subject(principal.getName())
                .audience(Collections.singletonList(registeredClient.getClientId()))
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .notBefore(issuedAt)
                .id(idStr);
        if (!CollectionUtils.isEmpty(context.getAuthorizedScopes())) {
            claimsBuilder.claim(OAuth2ParameterNames.SCOPE, context.getAuthorizedScopes());
        }
        if (this.accessTokenCustomizer != null) {
            // @formatter:off
            OAuth2TokenClaimsContext.Builder accessTokenContextBuilder = OAuth2TokenClaimsContext.with(claimsBuilder)
                    .registeredClient(context.getRegisteredClient())
                    .principal(context.getPrincipal())
                    .authorizationServerContext(context.getAuthorizationServerContext())
                    .authorizedScopes(context.getAuthorizedScopes())
                    .tokenType(context.getTokenType())
                    .authorizationGrantType(context.getAuthorizationGrantType());
            if (context.getAuthorization() != null) {
                accessTokenContextBuilder.authorization(context.getAuthorization());
            }
            if (context.getAuthorizationGrant() != null) {
                accessTokenContextBuilder.authorizationGrant(context.getAuthorizationGrant());
            }
            // @formatter:on

            OAuth2TokenClaimsContext accessTokenContext = accessTokenContextBuilder.build();
            this.accessTokenCustomizer.customize(accessTokenContext);
        }
        // @formatter:on
        OAuth2TokenClaimsSet accessTokenClaimsSet = claimsBuilder.build();
        aecEncryptionStringKeyGenerator = new AecEncryptionStringKeyGenerator(registeredClient.getClientSecret(), registeredClient.getClientId(), idStr);
//        String tokenValue = aecEncryptionStringKeyGenerator.generateKey();

        String tokenValue = new Snowflake().nextIdStr();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                tokenValue, accessTokenClaimsSet.getIssuedAt(), accessTokenClaimsSet.getExpiresAt());


        return accessToken;
    }

    public void setAccessTokenCustomizer(OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer) {
        this.accessTokenCustomizer = accessTokenCustomizer;
    }
}
