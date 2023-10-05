package com.hojung.junchef.service.recipe;

import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.util.error.exception.JunChefException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static com.hojung.junchef.util.error.exception.JunChefExceptionContent.SEARCH_WORD_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

public class RecipeGptServiceTest {
    @Mock
    private RecipeGptService recipeGptService;

    @DisplayName("음식 이름을 입력 시 해당 음식에 대한 레시피 반환")
    @Test
    void testSearchRecipe_Success() {
        // given
        String recipeName = "테스트";

        Recipe recipe = Recipe.builder().recipeName(recipeName)
                .cookingOrder(recipeName + " 재료")
                .cookingOrder(recipeName + " 만드는 방법")
                .build();

        given(recipeGptService.search(recipeName)).willReturn(recipe);

        // when
        Recipe result = recipeGptService.search(recipeName);

        // then
        assertEquals(result.getRecipeName(), recipe.getRecipeName());
        assertEquals(result.getIngredients(), recipe.getIngredients());
        assertEquals(result.getCookingOrder(), recipe.getCookingOrder());
    }

    @DisplayName("오탈자에 대한 예외 처리")
    @Test
    void testSearchRecipe_Failure() {
        // given
        String errorSearchWord = "테스트2";

        given(recipeGptService.search(errorSearchWord)).willThrow(new JunChefException(SEARCH_WORD_ERROR));

        // when
        Throwable thrown = catchThrowable(
                () -> recipeGptService.search(errorSearchWord)
        );

        // then
        assertThat(thrown)
                .isInstanceOf(JunChefException.class)
                .hasMessageContaining(SEARCH_WORD_ERROR.getMessage());
    }
}
