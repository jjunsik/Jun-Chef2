package com.hojung.junchef.domain.recipe;

import com.hojung.junchef.domain.BaseEntity;
import jakarta.persistence.*;
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

    @Column(length = 500)
    private String ingredients;

    @Column(length = 1000)
    private String cookingOrder;

    @Builder
    public Recipe(String recipeName, String ingredients, String cookingOrder) {
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.cookingOrder = cookingOrder;
    }
}
