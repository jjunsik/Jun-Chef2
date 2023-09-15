package com.hojung.junchef.repository.recipe;

import com.hojung.junchef.domain.recipe.Recipe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(RecipeRepositoryImpl.class)
class RecipeRepositoryTest {
    @Autowired
    private RecipeRepository recipeRepository;

    @DisplayName("레시피를 저장합니다.")
    @Test
    void saveRecipe() {
        // given
        Recipe recipe1 = createRecipe(1);

        // when
        recipe1 = recipeRepository.save(recipe1);

        //then
        Recipe result = recipeRepository.findById(recipe1.getId()).get();
        assertThat(recipe1).isEqualTo(result);
    }

    @DisplayName("해당 아이디와 일치하는 레시피를 조회합니다.")
    @Test
    void getRecipeById() {
        // given
        Recipe recipe1 = createRecipe(1);
        recipe1 = recipeRepository.save(recipe1);

        // when
        Recipe result = recipeRepository.findById(recipe1.getId()).get();

        // then
        assertThat(recipe1).isEqualTo(result);
    }

    @DisplayName("해당 이름과 일치하는 레시피를 조회합니다.")
    @Test
    void getRecipeByName() {
        // given
        Recipe recipe1 = createRecipe(1);
        recipe1 = recipeRepository.save(recipe1);

        // when
        Recipe result = recipeRepository.findByName(recipe1.getRecipeName()).get();

        // then
        assertThat(recipe1).isEqualTo(result);
    }

    private Recipe createRecipe(int number) {
        return Recipe.builder()
                .recipeName("testRecipeName" + number)
                .result("testResult" + number)
                .build();
    }
}