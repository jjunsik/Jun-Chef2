package com.hojung.junchef.repository.recipe;

import com.hojung.junchef.domain.recipe.Recipe;

import java.util.Optional;

public interface RecipeRepository {
    Recipe save(Recipe recipe);
    Optional<Recipe> findById(Long id);
    Optional<Recipe> findByName(String name);
}
