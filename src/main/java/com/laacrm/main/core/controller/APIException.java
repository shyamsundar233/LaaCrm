package com.laacrm.main.core.controller;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
public class APIException extends RuntimeException {

    @Getter
    @Setter
    public static class ErrorResponse {
        private String code;
        private String message;
        private JSONObject details;
    }

    private final ErrorResponse response;

    public APIException(int code, String message) {
        super(message);
        this.response = new ErrorResponse();
        this.response.setCode(String.valueOf(code));
        this.response.setMessage(message);
    }

    public APIException(int code, String message, JSONObject details) {
        super(message);
        this.response = new ErrorResponse();
        this.response.setCode(String.valueOf(code));
        this.response.setMessage(message);
        this.response.setDetails(details);
    }

}
