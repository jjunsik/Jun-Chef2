package com.hojung.junchef.repository.recipe;

import com.hojung.junchef.domain.recipe.Recipe;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class RecipeRepositoryImpl implements RecipeRepository {
    private final EntityManager em;

    @Override
    public Recipe save(Recipe recipe) {
        em.persist(recipe);

        return recipe;
    }

    @Override
    public Optional<Recipe> findById(Long id) {
        Recipe recipe = em.find(Recipe.class, id);

        return Optional.ofNullable(recipe);
    }

    @Override
    public Optional<Recipe> findByName(String name) {
        List<Recipe> result = em.createQuery("select r from Recipe r where r.recipeName = :recipeName", Recipe.class)
                .setParameter("recipeName", name)
                .getResultList();

        return result.stream().findAny();
    }
}
