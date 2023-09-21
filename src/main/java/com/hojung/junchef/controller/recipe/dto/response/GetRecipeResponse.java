package com.hojung.junchef.controller.recipe.dto.response;

import com.hojung.junchef.domain.recipe.Recipe;
import lombok.Data;

@Data
public class GetRecipeResponse {
    private final String recipeName;
    private final String ingredients;
    private final String cookingOrder;

    public GetRecipeResponse(Recipe recipe) {
        this.recipeName = recipe.getRecipeName();
        this.ingredients = recipe.getIngredients();
        this.cookingOrder = recipe.getCookingOrder();
    }
}
