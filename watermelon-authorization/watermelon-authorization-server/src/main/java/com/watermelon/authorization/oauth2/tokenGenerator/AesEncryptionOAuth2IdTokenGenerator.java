package com.watermelon.authorization.oauth2.tokenGenerator;

import com.watermelon.authorization.defaultauth.builtin.dto.SysUserDto;
import com.watermelon.authorization.defaultauth.support.phone.PhoneCaptchaAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * token 自定义 Generator
 * @author byh
 * @description
 */
public class AesEncryptionOAuth2IdTokenGenerator implements OAuth2TokenGenerator<Jwt> {


    @Override
    public Jwt generate(OAuth2TokenContext context) {
        if (!new OAuth2TokenType(OidcParameterNames.ID_TOKEN).equals(context.getTokenType())) {
            return null;
        }
        RegisteredClient registeredClient = context.getRegisteredClient();
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive());
        Long userId = null;
        Map<String, Object> claims = new HashMap<>();
        Authentication principal = context.getPrincipal();//这个地方实际上就是 provider 中的  DefaultOAuth2TokenContext.builder().principal(authentication) 中的 authentication
        if((principal instanceof UsernamePasswordAuthenticationToken||principal instanceof PhoneCaptchaAuthenticationToken)){
            SysUserDto sysUserDto = (SysUserDto)principal.getPrincipal();
            userId = sysUserDto.getId();
            claims.put("id",sysUserDto.getId());
            claims.put("phone",sysUserDto.getPhone());
            claims.put("username",sysUserDto.getUsername());
        }
        AecEncryptionStringKeyGenerator aecEncryptionStringKeyGenerator = new AecEncryptionStringKeyGenerator(userId,
                registeredClient.getClientId(),
                registeredClient.getClientSecret(),context.getAuthorizedScopes()
        );
        String tokenValue = aecEncryptionStringKeyGenerator.generateKey();
        return Jwt
                .withTokenValue(tokenValue)
                .expiresAt(expiresAt)
                .issuedAt(issuedAt)
                .subject(userId.toString())
                .claims((c) -> c.putAll(claims))
                .headers((c) -> c.putAll(claims))
                .build();
    }
}
