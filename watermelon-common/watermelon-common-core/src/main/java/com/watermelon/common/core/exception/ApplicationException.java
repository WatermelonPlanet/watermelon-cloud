package com.watermelon.common.core.exception;

/**
 * 业务异常用这个
 * @author byh
 * @description
 */
public class ApplicationException extends RuntimeException{

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
