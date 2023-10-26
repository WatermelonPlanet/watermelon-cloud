package com.watermelon.common.core.exception;

/**
 * 异常
 * @author byh
 * @description
 */
public class SystemException extends RuntimeException{

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
