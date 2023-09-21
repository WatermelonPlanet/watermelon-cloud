package com.watermelon.authorization.support.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 手机号+验证码 验证数据对象
 * @author byh
 * @date 2023-09-18
 * @description
 */
public class SmsCodeValidAuthenticationToken extends AbstractAuthenticationToken {
    /**
     * 手机号
     */
    private final String phone;
    /**
     * 验证码
     */
    private final String code;

    public SmsCodeValidAuthenticationToken(String phone, String code) {
        super(null);
        this.phone = phone;
        this.code = code;
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return phone;
    }

    public String getPhone() {
        return phone;
    }

    public String getCode() {
        return code;
    }

}
