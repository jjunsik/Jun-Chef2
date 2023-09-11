package com.hojung.junchef.service.recipe;

import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.repository.recipe.RecipeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
@Transactional
class RecipeServiceTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private RecipeGptService recipeGptService;

    @DisplayName("레시피 이름과 일치하는 레시피를 RecipeRepository 에서 조회하여 레시피를 반환")
    @Test
    void getRecipeByRepository() {
        // given
        Recipe recipe1 = getRecipe(1);
        recipeRepository.save(recipe1);

        // when
        Recipe recipe = recipeService.findByName(recipe1.getRecipeName());

        // then
        assertThat(recipe.getId()).isEqualTo(recipe1.getId());
    }

    @DisplayName("레시피 이름과 일치하는 레시피가 RecipeRepository 에 없을 경우, ChatGPT 와 통신하여 레시피 얻어서 반환")
    @Test
    void getRecipeByChatGPT() {
        // given
        String invalidRecipeName = "testRecipeName";

        // when
        Recipe recipe = recipeService.findByName(invalidRecipeName);

        // then
        assertThat(invalidRecipeName).isEqualTo(recipe.getRecipeName());
    }

    @DisplayName("해당 id 값을 RecipeRepository 에서 조회하여 레시피를 반환")
    @Test
    void findById() {
        // given
        Recipe recipe1 = getRecipe(1);
        recipeRepository.save(recipe1);

        // when
        Recipe recipe = recipeService.findById(recipe1.getId());

        // then
        assertThat(recipe.getId()).isEqualTo(recipe1.getId());
    }

    @DisplayName("해당 id 값이 RecipeRepository 에 존재하지 않을 경우, IllegalStateException 을 throw")
    @Test
    void findInvalidId() {
        // given
        String nonExistRecipeErrorMsg = "없는 레시피";
        Long illegalId = 99L;

        Recipe recipe1 = getRecipe(1);
        recipeRepository.save(recipe1);

        // when
        Throwable thrown = catchThrowable(
                () -> {
                    recipeService.findById(illegalId);
                });

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(nonExistRecipeErrorMsg);
    }

    private Recipe getRecipe(int number) {
        return Recipe.builder()
                .recipeName("testRecipeName" + number)
                .result("testResult" + number)
                .build();
    }
}