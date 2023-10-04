package com.hojung.junchef.util.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JunChefExceptionResponse {
    private final int code;
    private final String title;
    private final String message;

    public JunChefExceptionResponse(JunChefException junChefException) {
        JunChefExceptionContent junChefExceptionContent = junChefException.getJunChefExceptionContent();

        this.code = junChefExceptionContent.getCode();
        this.title = junChefExceptionContent.getTitle();
        this.message = junChefExceptionContent.getMessage();
    }
}
