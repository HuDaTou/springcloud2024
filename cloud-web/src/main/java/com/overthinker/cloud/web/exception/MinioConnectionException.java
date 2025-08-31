package com.overthinker.cloud.web.exception;

public class MinioConnectionException extends RuntimeException {
    public MinioConnectionException(String message) {
        super(message);
    }

    public MinioConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}