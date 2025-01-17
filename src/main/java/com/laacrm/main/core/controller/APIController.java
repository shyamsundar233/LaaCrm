package com.laacrm.main.core.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class APIController {

    @Getter
    @Setter
    public static class APIResponse {
        private String code;
        private String message;
        private Map<String, Object> data;
    }

    private final APIResponse apiResponse = new APIResponse();

    public void addResponse(int statusCode, String message) {
        addResponse(statusCode, message, null);
    }

    public void addResponse(int statusCode, String message, Map<String, Object> details) {
        apiResponse.code = String.valueOf(statusCode);
        apiResponse.message = message;
        apiResponse.data = details;
    }

    public ResponseEntity<APIResponse> response() {
        if(apiResponse.code == null || apiResponse.code.isEmpty()) {
            apiResponse.code = String.valueOf(HttpStatus.OK.value());
            apiResponse.message = "Response Structure has not been configured";
        }
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(Integer.parseInt(apiResponse.code)));
    }

}
