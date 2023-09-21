package com.watermelon.authorization.support.sms;

import com.alibaba.nacos.common.utils.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;


/**
 * 验证码验证  Provider 处理
 *
 * @author byh
 * @date 2023-09-18 17:09
 * @description
 */
public class SmsCodeValidAuthenticationProvider implements AuthenticationProvider {


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeValidAuthenticationToken smsCodeValidAuthenticationToken = (SmsCodeValidAuthenticationToken) authentication;
        String phone = smsCodeValidAuthenticationToken.getPhone();
        //验证码
        String code = smsCodeValidAuthenticationToken.getCode();
        if (!StringUtils.hasText(code)) {
            throw new OAuth2AuthenticationException("验证码不能为空!");
        }
        //todo 暂时先写000000 ，发送验证码的我们还没有写的
        if (!code.equals("000000")) {
            throw new OAuth2AuthenticationException("验证码：【" + code + "】已过期!");
        }
        //使用 UsernamePasswordAuthenticationToken 返回
        return new UsernamePasswordAuthenticationToken(phone, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeValidAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
