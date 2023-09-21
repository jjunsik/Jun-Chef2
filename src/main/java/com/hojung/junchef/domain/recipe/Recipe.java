package com.hojung.junchef.domain.recipe;

import com.hojung.junchef.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Recipe extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String recipeName;
    private String ingredients;
    private String cookingOrder;

    @Builder
    public Recipe(String recipeName, String ingredients, String cookingOrder) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.cookingOrder = cookingOrder;
    }
}
