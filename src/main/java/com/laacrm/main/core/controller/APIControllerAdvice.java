package com.laacrm.main.core.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestControllerAdvice
public class APIControllerAdvice {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIException.ErrorResponse> handleAPIException(APIException exp) {
        return new ResponseEntity<>(exp.getResponse(), HttpStatus.valueOf(Integer.parseInt(exp.getResponse().getCode())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIException.ErrorResponse> handleException(Exception exp) {
        final Logger LOGGER = Logger.getLogger(exp.getClass().getName());
        LOGGER.log(Level.SEVERE, exp.getMessage());
        exp.printStackTrace();
        APIException.ErrorResponse response = new APIException.ErrorResponse();
        response.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
        response.setMessage(exp.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
