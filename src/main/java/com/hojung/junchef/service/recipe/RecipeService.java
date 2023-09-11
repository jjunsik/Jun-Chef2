package com.hojung.junchef.service.recipe;

import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.repository.recipe.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeGptService recipeGptService;

    String nonExistRecipeErrorMsg = "없는 레시피";

    public Recipe findByName(String recipeName) {
        return recipeRepository.findByName(recipeName)
                .orElseGet(
                        () -> getRecipeFromChatGPT(recipeName)
                );
    }

    // findByName() 처럼 gpt 와 통신을 해서 레시피를 얻어와야 하는지 아직은 모르겠음.
    public Recipe findById(Long recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(
                        () -> new IllegalStateException(nonExistRecipeErrorMsg)
                );
    }

    private Recipe getRecipeFromChatGPT(String recipeName) {
        String result = recipeGptService.search(recipeName);

        Recipe recipe = Recipe.builder()
                .recipeName(recipeName)
                .result(result)
                .build();

        return recipeRepository.save(recipe);
    }
}
