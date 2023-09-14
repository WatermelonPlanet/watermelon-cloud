package com.watermelon.common.core.exception;

/**
 * 异常
 * @author byh
 * @date 2023-09-14
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
