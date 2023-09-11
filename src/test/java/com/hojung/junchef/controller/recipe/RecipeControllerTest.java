package com.hojung.junchef.controller.recipe;

import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.service.recipe.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RecipeController.class)
class RecipeControllerTest {
    @MockBean
    RecipeService recipeService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    void getRecipe() throws Exception {
        // given
        Recipe recipe = Recipe.builder()
                .recipeName("testRecipeName")
                .result("testResult")
                .build();

        given(recipeService.findByName(recipe.getRecipeName())).willReturn(recipe);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/jun-chef/v1/recipe")
                .param("search", recipe.getRecipeName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeName").exists())
                .andExpect(jsonPath("$.result").exists());

        then(recipeService).should().findByName(recipe.getRecipeName());
    }
}