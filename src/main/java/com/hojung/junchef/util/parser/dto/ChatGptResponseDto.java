package com.hojung.junchef.util.parser.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatGptResponseDto {
    List<ChatGptResponseChoicesDto> choices;
}
