package com.watermelon.authorization.oauth2.support.sms;


import com.watermelon.authorization.consent.AuthorizationServerConfigurationConsent;
import com.watermelon.authorization.util.OAuth2AuthorizationUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * sms  Converter
 *
 * @author byh
 * @date: 2023-09-18
 */
public final class SmsAuthenticationConverter implements AuthenticationConverter {

    @Nullable
    @Override
    public Authentication convert(HttpServletRequest request) {
        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!AuthorizationServerConfigurationConsent.GRANT_TYPE_SMS_CODE.equals(grantType)) {
            return null;
        }
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        //OAuth2AuthorizationUtils 是copy 源码中存在的
        MultiValueMap<String, String> parameters = OAuth2AuthorizationUtils.getParameters(request);


        // scope (OPTIONAL)
        Set<String> scopes = null;
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) &&
                parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            OAuth2AuthorizationUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE, null);
        }
        if (StringUtils.hasText(scope)) {
            scopes = new HashSet<>(
                    Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
        }


        // phone (REQUIRED) 手机号
        String phone = parameters.getFirst(AuthorizationServerConfigurationConsent.OAUTH2_PARAMETER_NAME_PHONE);
        if (!StringUtils.hasText(phone) ||
                parameters.get(AuthorizationServerConfigurationConsent.OAUTH2_PARAMETER_NAME_PHONE).size() != 1) {
            OAuth2AuthorizationUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "OAuth 2.0 Parameter: " + AuthorizationServerConfigurationConsent.OAUTH2_PARAMETER_NAME_PHONE,
                    null);
        }

        // sms_code (REQUIRED) 验证码必填
        String smsCode = parameters.getFirst(AuthorizationServerConfigurationConsent.OAUTH2_PARAMETER_NAME_SMS_CODE);
        if (!StringUtils.hasText(smsCode) ||
                parameters.get(AuthorizationServerConfigurationConsent.OAUTH2_PARAMETER_NAME_SMS_CODE).size() != 1) {
            OAuth2AuthorizationUtils.throwError(
                    OAuth2ErrorCodes.INVALID_REQUEST,
                    "OAuth 2.0 Parameter: " + AuthorizationServerConfigurationConsent.OAUTH2_PARAMETER_NAME_SMS_CODE,
                    null);
        }

        //扩展参数
        Map<String, Object> additionalParameters = new HashMap<>();
        parameters.forEach((key, value) -> {
            if (!key.equals(OAuth2ParameterNames.GRANT_TYPE)) {
                additionalParameters.put(key, value.get(0));
            }
        });

        return new SmsAuthenticationToken(new AuthorizationGrantType(AuthorizationServerConfigurationConsent.GRANT_TYPE_SMS_CODE),
                clientPrincipal,
                scopes,
                phone,
                smsCode,
                additionalParameters);
    }
}
