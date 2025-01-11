package com.laacrm.main.core.controller;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
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

    public void addResponse(int statusCode, String message, JSONObject response) {
        apiResponse.code = String.valueOf(statusCode);
        apiResponse.message = message;
        if(response != null && !response.isEmpty()) {
            apiResponse.data = new HashMap<>();
            for(String data : response.keySet()){
                apiResponse.data.put(data, response.get(data));
            }
        }
    }

    public ResponseEntity<APIResponse> response() {
        return new ResponseEntity<>(apiResponse, HttpStatus.valueOf(Integer.parseInt(apiResponse.code)));
    }

}
