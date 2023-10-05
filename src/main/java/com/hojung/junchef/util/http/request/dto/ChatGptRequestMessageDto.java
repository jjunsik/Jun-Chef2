package com.hojung.junchef.util.http.request.dto;

public class ChatGptRequestMessageDto {
    String role = "user";
    String content;

    //    public static final String FIXED_MESSAGE1 = "내가 입력한 레시피 이름에서 띄어쓰기를 제외한 나머지 이름과 정확하게 일치하는 레시피가 있는 경우에만 알려주고 그렇지 않은 경우에는 모른다고 답변해.";
    public static final String FIXED_MESSAGE2 = " 레시피를 다른 말하지 말고 [재료]와 [레시피]만 알려줘. ";

    public ChatGptRequestMessageDto(String content) {
        this.content = content + FIXED_MESSAGE2;
    }
}
