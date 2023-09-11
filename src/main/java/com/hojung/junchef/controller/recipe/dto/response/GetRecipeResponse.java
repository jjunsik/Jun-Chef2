package com.hojung.junchef.controller.recipe.dto.response;

import com.hojung.junchef.domain.recipe.Recipe;
import lombok.Data;

@Data
public class GetRecipeResponse {
    private final String recipeName;
    private final String result;

    public GetRecipeResponse(Recipe recipe) {
        this.recipeName = recipe.getRecipeName();
        this.result = recipe.getResult();
    }
}
