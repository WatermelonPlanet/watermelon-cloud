package com.watermelon.authorization.oauth.client.exception;

/**
 * 异常
 * @author byh
 * @date 2023-09-21
 * @description
 */
public class OAuth2UserConvertException extends RuntimeException{

    public OAuth2UserConvertException(String message) {
        super(message);
    }

    public OAuth2UserConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
