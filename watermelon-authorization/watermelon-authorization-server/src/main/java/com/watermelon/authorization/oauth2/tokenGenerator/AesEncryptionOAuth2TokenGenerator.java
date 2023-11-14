package com.watermelon.authorization.oauth2.tokenGenerator;

import com.watermelon.authorization.defaultauth.builtin.dto.SysUserDto;
import com.watermelon.authorization.defaultauth.support.phone.PhoneCaptchaAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.*;
import java.time.Instant;
/**
 * token 自定义 Generator
 * @author byh
 * @description
 */
public class AesEncryptionOAuth2TokenGenerator implements OAuth2TokenGenerator<OAuth2AccessToken> {


    @Override
    public OAuth2AccessToken generate(OAuth2TokenContext context) {
        if (!OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
            return null;
        }


        RegisteredClient registeredClient = context.getRegisteredClient();

        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive());


        Long userId = null;
        Authentication principal = context.getPrincipal();//这个地方实际上就是 provider 中的  DefaultOAuth2TokenContext.builder().principal(authentication) 中的 authentication
        if((principal instanceof UsernamePasswordAuthenticationToken||principal instanceof PhoneCaptchaAuthenticationToken)){
            SysUserDto sysUserDto = (SysUserDto)principal.getPrincipal();
            userId = sysUserDto.getId();
        }

//      claimsBuilder.claim(OAuth2ParameterNames.SCOPE, );

        AecEncryptionStringKeyGenerator aecEncryptionStringKeyGenerator = new AecEncryptionStringKeyGenerator(userId,
                registeredClient.getClientId(),
                registeredClient.getClientSecret(),context.getAuthorizedScopes()
        );
        String tokenValue = aecEncryptionStringKeyGenerator.generateKey();

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                tokenValue, issuedAt, expiresAt);


        return accessToken;
    }
}
