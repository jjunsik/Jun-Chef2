package com.hojung.junchef.util.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class JunChefExceptionHandler {
    @ExceptionHandler(JunChefException.class)
    public ResponseEntity<JunChefExceptionResponse> handlerJunChefException(JunChefException j) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new JunChefExceptionResponse(j));
    }
}
