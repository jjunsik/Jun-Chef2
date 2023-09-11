package com.hojung.junchef.controller.recipe;

import com.hojung.junchef.controller.recipe.dto.response.GetRecipeResponse;
import com.hojung.junchef.domain.recipe.Recipe;
import com.hojung.junchef.service.recipe.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("jun-chef/v1/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public GetRecipeResponse getRecipe(@RequestParam("search") String recipeName) {
        Recipe recipe = recipeService.findByName(recipeName);

        return new GetRecipeResponse(recipe);
    }
}
