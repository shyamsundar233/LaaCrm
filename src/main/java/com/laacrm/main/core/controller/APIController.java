package com.laacrm.main.core.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class APIController {

    private ResponseEntity<JSONObject> response;

    public void addResponse(int statusCode, JSONObject response) {
        this.response = new ResponseEntity<>(response, HttpStatus.valueOf(statusCode));
    }

    public ResponseEntity<JSONObject> response() {
        return response;
    }

}
