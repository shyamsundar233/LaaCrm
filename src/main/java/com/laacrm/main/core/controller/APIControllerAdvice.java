package com.laacrm.main.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class APIControllerAdvice {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIException.ErrorResponse> handleAPIException(APIException exp) {
        return new ResponseEntity<>(exp.getResponse(), HttpStatus.valueOf(Integer.parseInt(exp.getResponse().getCode())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIException.ErrorResponse> handleException(Exception exp) {
        APIException.ErrorResponse response = new APIException.ErrorResponse();
        response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        response.setMessage(exp.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
