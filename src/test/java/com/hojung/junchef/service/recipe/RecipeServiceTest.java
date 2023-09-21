package com.hojung.junchef.service.recipe;

import com.hojung.junchef.domain.member.Member;
import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.repository.recipe.RecipeRepository;
import com.hojung.junchef.service.member.MemberService;
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
    private MemberService memberService;


    @DisplayName("해당 레시피 이름과 일치하는 레시피를 RecipeRepository 에서 조회하여 레시피를 반환")
    @Test
    void getRecipeByRepository() {
        // given
        Recipe recipe1 = createRecipe(1);
        recipe1 = recipeRepository.save(recipe1);

        Member member1 = createMember(1);
        Long memberId = memberService.join(member1);

        // when
        Recipe recipe = recipeService.findByName(memberId, recipe1.getRecipeName());

        // then
        assertThat(recipe.getId()).isEqualTo(recipe1.getId());
    }

    @DisplayName("레시피 이름과 일치하는 레시피가 RecipeRepository 에 없을 경우, ChatGPT 와 통신하여 레시피 얻어서 반환")
    @Test
    void getRecipeByChatGPT() {
        // given
        String invalidRecipeName = "testRecipeName";

        Member member1 = createMember(1);
        Long memberId = memberService.join(member1);

        // when
        Recipe recipe = recipeService.findByName(memberId, invalidRecipeName);

        // then
        assertThat(invalidRecipeName).isEqualTo(recipe.getRecipeName());
    }

    @DisplayName("해당 레시피의 id 값과 일치하는 레시피를 RecipeRepository 에서 조회하여 반환")
    @Test
    void findById() {
        // given
        Recipe recipe1 = createRecipe(1);
        recipe1 = recipeRepository.save(recipe1);

        Member member1 = createMember(1);
        Long memberId = memberService.join(member1);

        // when
        Recipe recipe = recipeService.findById(memberId, recipe1.getId());

        // then
        assertThat(recipe.getId()).isEqualTo(recipe1.getId());
    }

    @DisplayName("해당 레시피의 id 값이 RecipeRepository 에 존재하지 않을 경우, IllegalStateException 을 throw")
    @Test
    void findInvalidId() {
        // given
        String nonExistRecipeErrorMsg = "없는 레시피";
        Long illegalId = 99L;

        Recipe recipe1 = createRecipe(1);
        recipeRepository.save(recipe1);

        Member member1 = createMember(1);
        Long memberId = memberService.join(member1);

        // when
        Throwable thrown = catchThrowable(
                () -> {
                    recipeService.findById(memberId, illegalId);
                });

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(nonExistRecipeErrorMsg);
    }

    @DisplayName("레시피 이름이 항상 공백이 제거돼서 DB 에 저장되는지 확인")
    @Test
    void recipeNameWithSpacesRemoved() {
        // given
        Member member1 = createMember(1);
        Long memberId = memberService.join(member1);

        Recipe recipe1 = Recipe.builder()
                .recipeName("Test RecipeName")
                .ingredients("testIngredients")
                .cookingOrder("testCookingOrder")
                .build();

        // when
        Recipe getRecipe = recipeService.findByName(memberId, recipe1.getRecipeName());

        // then
        assertThat(getRecipe.getRecipeName()).isEqualTo(recipe1.getRecipeName().replaceAll("\\s+", ""));
    }

    @DisplayName("RecipeRepository 에 존재하는 음식 이름을 검색했을 때 반환되는 객체가 공백 여부와 상관없이 같은 객체인지 확인")
    @Test
    void getRecipeInRecipeRepository() {
        // given
        Member member1 = createMember(1);
        Long memberId = memberService.join(member1);

        String testRecipeName = "Test Recipe Name";
        String removeSpacesTestRecipeName = testRecipeName.replaceAll("\\s+", "");

        Recipe compareRecipe = recipeService.findByName(memberId, testRecipeName);

        // when
        Recipe removeSpacesRecipe = recipeService.findByName(memberId, removeSpacesTestRecipeName);
        Recipe noRemoveSpacesRecipe = recipeService.findByName(memberId, testRecipeName);

        // then
        assertThat(compareRecipe.getRecipeName()).isEqualTo(removeSpacesRecipe.getRecipeName());
        assertThat(compareRecipe.getRecipeName()).isEqualTo(noRemoveSpacesRecipe.getRecipeName());
    }

    private Recipe createRecipe(int number) {
        return Recipe.builder()
                .recipeName("testRecipeName" + number)
                .ingredients("testIngredients" + number)
                .cookingOrder("testCookingOrder" + number)
                .build();
    }

    private Member createMember(int number) {
        return Member.builder()
                .email("testEmail" + number + "@test.com")
                .name("testName" + number)
                .passwd("testPW" + number)
                .build();
    }
}