package com.hojung.junchef.util.http;

import com.google.gson.Gson;
import com.hojung.junchef.util.http.request.ChatGptRequest;
import com.hojung.junchef.util.http.request.dto.ChatGptRequestMessageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatGptHttpServiceTest {
    private final ChatGptHttpService chatGptHttpService = new ChatGptHttpService();

    private final List<String> message = new ArrayList<>();

    @DisplayName("ChatGPT API 통신이 잘 되는지 확인")
    @Test
    void search() throws IOException {
        // given
        String recipeName = "라면";

        ChatGptRequestMessageDto chatGptRequestMessageDto = new ChatGptRequestMessageDto(recipeName);

        Gson gson = new Gson();
        message.add(gson.toJson(chatGptRequestMessageDto));

        ChatGptRequest request = new ChatGptRequest(message);

        // when
        String response = chatGptHttpService.search(request);

        // then
        assertThat(response).contains("id");
        assertThat(response).contains("object");
        assertThat(response).contains("created");
        assertThat(response).contains("model");
        assertThat(response).contains("choices");
        assertThat(response).contains("usage");
    }
}