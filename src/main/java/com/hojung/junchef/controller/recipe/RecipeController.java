package com.hojung.junchef.controller.recipe;

import com.hojung.junchef.controller.recipe.dto.response.GetRecipeResponse;
import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.service.recipe.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("jun-chef/v1/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/{memberId}")
    public GetRecipeResponse getRecipe(@PathVariable("memberId") Long memberId, @RequestParam("search") String recipeName) {
        Recipe recipe = recipeService.findByName(memberId, recipeName);

        return new GetRecipeResponse(recipe);
    }
}
