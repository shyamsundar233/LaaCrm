package com.laacrm.main.framework.exception;

public class FrameworkException extends RuntimeException {

    private int code;

    public FrameworkException(int code, String message) {
        super(message);
        this.code = code;
    }
}
