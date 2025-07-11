package com.example.recipeapp.domain.recipes.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecipeSearchRequest {
    private String title;
    private String category;
    private String nickname;
}
