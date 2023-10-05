package com.hojung.junchef.util.http.request;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ChatGptRequest {
    private static final String CHAT_GPT_KEY = "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";
    private static final String CHAT_GPT_URL = "https://api.openai.com/v1/chat/completions";
    private static final String CHAT_GPT_MODEL = "gpt-3.5-turbo";
    private final List<String> messages;

    public String request() {
        return "{\"model\":\"" + CHAT_GPT_MODEL + "\"" + ",\"messages\":" + messages + "}";
    }

    public String getKey() {
        return CHAT_GPT_KEY;
    }

    public String getUrl() {
        return CHAT_GPT_URL;
    }
}
