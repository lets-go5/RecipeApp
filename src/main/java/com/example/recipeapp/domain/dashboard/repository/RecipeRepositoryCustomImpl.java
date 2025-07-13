package com.example.recipeapp.domain.dashboard.repository;

import com.example.recipeapp.domain.recipes.domain.model.Recipe;
import com.example.recipeapp.domain.recipes.domain.model.RecipeCategory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class RecipeRepositoryCustomImpl implements RecipeRepositoryCustom{
    @Override
    public Optional<Recipe> findCategoryTopRecipeOrderByLikes(RecipeCategory category, LocalDateTime start, LocalDateTime end) {
        return Optional.empty();
    }
}
