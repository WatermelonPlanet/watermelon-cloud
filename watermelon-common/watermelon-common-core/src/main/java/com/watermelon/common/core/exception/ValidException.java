package com.watermelon.common.core.exception;

/**
 * 验证异常
 * @author byh
 * @description
 */
public class ValidException extends RuntimeException{

    public ValidException(String message) {
        super(message);
    }

    public ValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
