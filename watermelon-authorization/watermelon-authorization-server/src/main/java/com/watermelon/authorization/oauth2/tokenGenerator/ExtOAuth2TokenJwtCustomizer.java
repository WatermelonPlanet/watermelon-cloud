package com.watermelon.authorization.oauth2.tokenGenerator;

import com.watermelon.authorization.consent.AuthorizationServerConfigurationConsent;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;


/**
 * jwt属性扩展
 *
 * @author byh
 * @date 2023-11-20 17:46
 * @description
 */
public class ExtOAuth2TokenJwtCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {


    @Override
    public void customize(JwtEncodingContext context) {
        JwtClaimsSet.Builder claims = context.getClaims();
        String tokenType = context.getTokenType().getValue();
        Object principal = context.getPrincipal().getPrincipal();
        if (principal instanceof DefaultOAuth2User defaultOAuth2User) {
            //todo 看后续是否需要根据第三方平台类型的map中属性不同再进行分别的一个转换
            claims.claim(AuthorizationServerConfigurationConsent.USER_INFO_PARAMETER,
                    defaultOAuth2User.getAttributes());
            return;
        }
        // 这个 Object principal 对应的是 provider 中 DefaultOAuth2TokenContext.Builder.principal(usernamePasswordAuthenticationToken) 这个地方的对象 可以根据需要自行扩展属性
        claims.claim(AuthorizationServerConfigurationConsent.USER_INFO_PARAMETER, principal);
    }
}
