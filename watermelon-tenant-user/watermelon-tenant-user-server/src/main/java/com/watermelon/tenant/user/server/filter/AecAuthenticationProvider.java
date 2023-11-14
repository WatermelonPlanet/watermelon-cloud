
package com.watermelon.tenant.user.server.filter;

import com.watermelon.common.core.util.AESUtil;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


public final class AecAuthenticationProvider implements AuthenticationProvider {

    private final Log logger = LogFactory.getLog(getClass());

    private final static String SEPARATOR = "::";
    private final static String COMMA = ",:";
    private final static String AEC_KYE = "c79112eb6e994b51a866b6481d36004b";

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAecAuthenticationToken bearer = (BearerTokenAecAuthenticationToken) authentication;
        String tokenValue = bearer.getToken();

        String token = AESUtil.decrypt(tokenValue, AEC_KYE);
        String[] tokenArray = token.split(SEPARATOR);
        if (tokenArray.length == 0) {
            throw new OAuth2AuthorizationException(new OAuth2Error("token is error!"));
        }
        Long userId = Long.parseLong(tokenArray[0]);
        String clientId = tokenArray[1];
        String clientSecret = tokenArray[2];
//        String[] scopesArray = tokenArray[3].split(COMMA);
//        Set<String> scopes = Arrays.stream(scopesArray).collect(Collectors.toSet());
        BearerUserAuthenticationDto userAuthenticationDto = new BearerUserAuthenticationDto(userId, clientId, clientSecret, null);
        return PhoneCaptchaAuthenticationToken.authenticated(userAuthenticationDto,authentication,null);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return BearerTokenAecAuthenticationToken.class.isAssignableFrom(authentication);
    }


}
