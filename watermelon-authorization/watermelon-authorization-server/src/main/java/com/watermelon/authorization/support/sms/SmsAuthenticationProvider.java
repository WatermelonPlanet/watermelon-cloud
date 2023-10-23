package com.watermelon.authorization.support.sms;

import com.watermelon.authorization.util.OAuth2AuthorizationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * @author byh
 * @date 2023-09-18
 * @description
 */
@Slf4j
public final class SmsAuthenticationProvider implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-3.2.1";

    private OAuth2AuthorizationService authorizationService;

    private OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    private AuthenticationManager authenticationManager;//暂时没有用到 如果额外加一个 短信验证码的 Provider 参考 DaoAuthenticationProvider

    private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE =
            new OAuth2TokenType(OidcParameterNames.ID_TOKEN);


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsAuthenticationToken smsAuthenticationToken = (SmsAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal =
                OAuth2AuthorizationUtils.getAuthenticatedClientElseThrowInvalidClient(smsAuthenticationToken);

        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        if (log.isTraceEnabled()) {
            log.trace("Retrieved registered client");
        }
        if (registeredClient == null) {
            throw new OAuth2AuthenticationException("client_id not exist");
        }

        try {
            //todo 验证码验证逻辑
            //验证码
            String code = smsAuthenticationToken.getCode();
            if (!StringUtils.hasText(code)) {
                throw new OAuth2AuthenticationException("验证码不能为空!");
            }
            //todo 暂时先写000000 ，发送验证码的我们还没有写的
            if (!code.equals("000000")) {
                throw new OAuth2AuthenticationException("验证码：【" + code + "】已过期!");
            }
            Authentication smsCodeValidAuthentication = smsAuthenticationToken;
            smsAuthenticationToken.setAuthenticated(true);
            // @formatter:off
            DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(smsCodeValidAuthentication)
                    .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                    .authorizedScopes(smsAuthenticationToken.getScopes())
                    .authorizationGrantType(smsAuthenticationToken.getAuthorizationGrantType())
                    .authorizationGrant(smsAuthenticationToken);
            // @formatter:on
            OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                    .withRegisteredClient(registeredClient)
                    .principalName(smsCodeValidAuthentication.getName())
                    .authorizationGrantType(smsAuthenticationToken.getAuthorizationGrantType())
                    .authorizedScopes(smsAuthenticationToken.getScopes());

            // ----- Access token -----
            OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
            OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
            if (generatedAccessToken == null) {
                OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                        "The token generator failed to generate the access token.", ERROR_URI);
                throw new OAuth2AuthenticationException(error);
            }

            if (log.isTraceEnabled()) {
                log.trace("Generated access token");
            }

            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                    generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                    generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
            if (generatedAccessToken instanceof ClaimAccessor) {
                authorizationBuilder.token(accessToken, (metadata) ->
                                metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()))
                        .attribute(Principal.class.getName(), smsCodeValidAuthentication);
            } else {
                authorizationBuilder.accessToken(accessToken);
            }

            // ----- Refresh token -----
            OAuth2RefreshToken refreshToken = null;
            if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                    // Do not issue refresh token to public client
                    !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

                tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
                OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
                if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                    OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                            "The token generator failed to generate the refresh token.", ERROR_URI);
                    throw new OAuth2AuthenticationException(error);
                }

                if (log.isTraceEnabled()) {
                    log.trace("Generated refresh token");
                }

                refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
                authorizationBuilder.refreshToken(refreshToken);
            }

            // ----- ID token -----
            OidcIdToken idToken;
            if (smsAuthenticationToken.getScopes().contains(OidcScopes.OPENID)) {

                // @formatter:off
                tokenContext = tokenContextBuilder
                        .tokenType(ID_TOKEN_TOKEN_TYPE)
                        .authorization(authorizationBuilder.build())    // ID token customizer may need access to the access token and/or refresh token
                        .build();
                // @formatter:on
                OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);
                if (!(generatedIdToken instanceof Jwt)) {
                    OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                            "The token generator failed to generate the ID token.", ERROR_URI);
                    throw new OAuth2AuthenticationException(error);
                }

                if (log.isTraceEnabled()) {
                    log.trace("Generated id token");
                }

                idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(),
                        generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
                authorizationBuilder.token(idToken, (metadata) ->
                        metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims()));
            } else {
                idToken = null;
            }

            OAuth2Authorization authorization = authorizationBuilder.build();


            this.authorizationService.save(authorization);

            if (log.isTraceEnabled()) {
                log.trace("Saved authorization");
            }

            Map<String, Object> additionalParameters = Collections.emptyMap();
            if (idToken != null) {
                additionalParameters = new HashMap<>();
                additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
            }

            if (log.isTraceEnabled()) {
                log.trace("Authenticated token request");
            }

            return new OAuth2AccessTokenAuthenticationToken(
                    registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
        } catch (Exception e) {

            OAuth2Error error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                    e.getMessage(), ERROR_URI);

            throw new OAuth2AuthenticationException(error);
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }


    public void setTokenGenerator(OAuth2TokenGenerator<?> tokenGenerator) {
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.tokenGenerator = tokenGenerator;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        Assert.notNull(authorizationService, "authenticationManager cannot be null");
        this.authenticationManager = authenticationManager;
    }

    public void setAuthorizationService(OAuth2AuthorizationService authorizationService) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        this.authorizationService = authorizationService;
    }

}
