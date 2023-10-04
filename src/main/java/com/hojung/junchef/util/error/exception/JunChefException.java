package com.hojung.junchef.util.error.exception;

import lombok.Getter;

@Getter
public class JunChefException extends RuntimeException{
    private final JunChefExceptionContent junChefExceptionContent;

    public JunChefException(JunChefExceptionContent content) {
        super(content.getMessage());
        this.junChefExceptionContent = content;
    }

    public JunChefException(JunChefExceptionContent content, Throwable e) {
        super(content.getMessage(), e);
        this.junChefExceptionContent = content;
    }
}
