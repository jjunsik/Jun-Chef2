package com.hojung.junchef.util.parser;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.util.parser.dto.ChatGptResponseChoicesDto;
import com.hojung.junchef.util.parser.dto.ChatGptResponseDto;
import com.hojung.junchef.util.parser.dto.ChatGptResponseMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.hojung.junchef.util.parser.constant.ChatGptParserConstant.*;

@Slf4j
@Service
public class ChatGptResponseParser {
    public Recipe getRecipeByResponse(String response) {
        StringBuilder ingredients = new StringBuilder(START_INGREDIENT_TEXT);
        StringBuilder cookingOrder = new StringBuilder(START_COOKING_ORDER_TEXT);

        Gson gson = new Gson();

        try {
            // 받은 response 를 ChatGptResponseDto 클래스 형식으로 파싱
            ChatGptResponseDto chatGptResponseDto = gson.fromJson(response, ChatGptResponseDto.class);

            // chatGptResponseDto, choiceDto, splitText 등의 객체가 null 인지 확인
            if(chatGptResponseDto == null || chatGptResponseDto.getChoices() == null || chatGptResponseDto.getChoices().isEmpty())
                return null;

            // responseDto에서 첫 번째 choice를 가져옴
            ChatGptResponseChoicesDto chatGptResponseChoicesDto = chatGptResponseDto.getChoices().get(0);

            if (chatGptResponseChoicesDto == null)
                return null;

            // chatGptResponseChoicesDto에서 message를 가져옴
            ChatGptResponseMessageDto chatGptResponseMessageDto = chatGptResponseChoicesDto.getMessage();

            if (chatGptResponseMessageDto == null)
                return null;

            // messageDto에서 content 값을 가져옴
            String msgContent = chatGptResponseMessageDto.getContent().trim();

            // 정규식을 사용하여 [재료]와 [레시피]로 문자열을 분할
            String[] splitContent = msgContent.split(INGREDIENT_RECIPE_REGEX);

            // 파싱 오류 처리
            // 분할된 문자열의 길이가 3인 경우 (0번째 -> 재료 나오기 전 말이나 \n, 1번째 -> [재료], 2번째 -> [레시피]) [재료]와 [레시피]의 값을 추출
            if(splitContent.length != 3) {
                log.info("파싱 오류 조건인 \"분할된 문자열 길이가 3이 아님\"을 만족해서 오류뜸.");
                return null;
            }

            ingredients.append("\n").append(splitContent[1].replace("\n\n", "\n").trim());
            cookingOrder.append("\n").append(splitContent[2].replace("\n\n", "\n").trim());
            log.info("분할 했을 때 길이가 일단 3이어서 재료와 레시피에 텍스트 넣어줌.");

            // 재료와 레시피에 담길 텍스트의 길이가 매우 짧은 경우
            if (parserErrorCondition(ingredients, cookingOrder)) {
                //파싱 안됨.
                log.info("파싱 오류 조건인 \"텍스트 길이 짧음\"을 만족해서 오류뜸.");
                return null;
            }

            log.info("파싱 잘 됨.");

        } catch (JsonSyntaxException j) {
            log.info("왜 객체 변환 불가?\n" + response);
            j.printStackTrace();
            throw j;
        }

        log.info("파싱 완료 Recipe 객체로 만들어서 전달");

        return Recipe.builder()
                .recipeName(RECIPE_NAME_TITLE)
                .ingredients(ingredients.toString().trim())
                .cookingOrder(cookingOrder.toString().trim())
                .build();
    }
}
