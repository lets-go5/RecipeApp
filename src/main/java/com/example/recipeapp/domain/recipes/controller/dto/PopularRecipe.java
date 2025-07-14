package com.example.recipeapp.domain.recipes.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) //직렬화 문제 방지
public class PopularRecipe {

    private String keyword;
    private List<RecipeResponse> topRecipes;

}
