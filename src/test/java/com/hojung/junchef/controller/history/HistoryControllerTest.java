package com.hojung.junchef.controller.history;

import com.google.gson.Gson;
import com.hojung.junchef.domain.history.History;
import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.service.history.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HistoryController.class)
class HistoryControllerTest {
    @MockBean
    HistoryService historyService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    static final String BASE_HISTORY_URL = "/jun-chef/v1/history";

    static final Long TEST_MEMBER_ID = 1L;

    static final Long TEST_HISTORY_ID = 99L;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .alwaysDo(print())
                .build();
    }

    @DisplayName("최근 검색어 목록을 조회")
    @Test
    void getHistoryList() throws Exception {
        // given
        History history1_1 = createHistory(1, 1);
        History history1_2 = createHistory(1, 2);

        given(historyService.findAllByMemberId(TEST_MEMBER_ID)).willReturn(List.of(history1_1, history1_2));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_HISTORY_URL + "/" + TEST_MEMBER_ID))
                .andExpect(status().isOk());

        then(historyService).should().findAllByMemberId(TEST_MEMBER_ID);
    }

    @DisplayName("최근 검색어를 삭제")
    @Test
    void delete() throws Exception {
        // given
        Gson gson = new Gson();
        String content = gson.toJson(TEST_HISTORY_ID);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_HISTORY_URL + "/" + TEST_HISTORY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                        .andExpect(status().isOk());
    }

    @DisplayName("레시피 이름으로 History 객체의 ID 값 반환")
    @Test
    void getHistoryIdByRecipeName() throws Exception{
        // given
        History history = createHistory(1, 1);
        String recipeName = history.getRecipe().getRecipeName();

        given(historyService.findByMemberIdAndRecipeName(TEST_MEMBER_ID, recipeName)).willReturn(history);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_HISTORY_URL + "/member/" + TEST_MEMBER_ID)
                        .param("recipeName", recipeName))
                        .andExpect(status().isOk());

        then(historyService).should().findByMemberIdAndRecipeName(TEST_MEMBER_ID, recipeName);
    }

    private History createHistory(int memberNumber, int recipeNumber) {
        return new History(createMember(memberNumber), createRecipe(recipeNumber));
    }

    private Member createMember(int memberNumber) {
        return Member.builder()
                .email("testEmail" + memberNumber + "@test.com")
                .name("testName" + memberNumber)
                .passwd("testPW" + memberNumber)
                .build();
    }

    private Recipe createRecipe(int recipeNumber) {
        return Recipe.builder()
                .recipeName("testRecipeName" + recipeNumber)
                .ingredients("testIngredients" + recipeNumber)
                .cookingOrder("testCookingOrder" + recipeNumber)
                .build();
    }
}