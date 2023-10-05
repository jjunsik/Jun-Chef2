package com.hojung.junchef.service.recipe;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.util.error.exception.JunChefException;
import com.hojung.junchef.util.http.ChatGptHttpService;
import com.hojung.junchef.util.http.request.ChatGptRequest;
import com.hojung.junchef.util.http.request.dto.ChatGptRequestMessageDto;
import com.hojung.junchef.util.parser.ChatGptResponseParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.hojung.junchef.util.error.constant.JunChefExceptionConstant.NUMBER_OF_SEARCHES;
import static com.hojung.junchef.util.error.exception.JunChefExceptionContent.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class RecipeGptService {
    public final ChatGptHttpService chatGptHttpService;
    public final ChatGptResponseParser chatGptResponseParser;
    public Recipe recipe;

    public Recipe search(String recipeName) {
        String response;

        List<String> message = new ArrayList<>();

        ChatGptRequestMessageDto chatGptRequestMessageDto = new ChatGptRequestMessageDto(recipeName);

        Gson gson = new Gson();
        message.add(gson.toJson(chatGptRequestMessageDto));

        int retryCount = 0;

        while (retryCount < NUMBER_OF_SEARCHES) {
            if(!recipeName.matches("[가-힣 ]*")) {
                log.info("검색어 오류");
                throw new JunChefException(SEARCH_WORD_ERROR);
            }

            response = getResponseByMessage(message);

            recipe = chatGptResponseParser.getRecipeByResponse(response);

            // 검색이 성공한 경우 결과를 반환
            if (recipe == null) {
                log.info("재검색 해서 실패");
                retryCount++;

                // 실행되는 스레드에 대한 제어
                sleep(1000);
                continue;
            }

            // RecipeRepository 에 Recipe 를 저장할 때, 무조건 음식 이름의 공백을 모두 제거하고 저장하기 위해 replaceAll 을 사용
            recipe.setRecipeName(recipeName.replaceAll("\\s+", ""));

            log.info("검색 성공");
            return recipe;
        }

        log.info("몇 번의 시도 끝에 검색 오류 전달");
        throw new JunChefException(NON_EXIST_RECIPE_ERROR);
    }

    private static String getResponseByMessage(List<String> message) {
        String response;

        try {
            response = new ChatGptHttpService().search(new ChatGptRequest(message));

            // 네트워크 연결 및 API 통신 예외 처리
        } catch (IOException e) {
            if (e instanceof UnknownHostException) {
                log.info("네트워크 연결 오류임.");
                throw new JunChefException(NETWORK_ERROR, e);
            }

            // 네트워크 연결 에러가 아닌 다른 네트워크 문제일 때
            log.info("네트워크 연결이 아닌 네트워크 통신 오류임.");
            throw new JunChefException(API_ERROR, e);

            // response 데이터를 자바 객체(GptResponseDto)로 변환 불가일 때
            // 파싱 불가(검색 불가)가 아님.
        } catch (JsonSyntaxException j) {
            log.info("객체 변환 불가 오류임.\n");
            throw new JunChefException(API_ERROR, j);
        }

        return response;
    }

    private static void sleep (int mills) {
        try {
            Thread.sleep(mills);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
