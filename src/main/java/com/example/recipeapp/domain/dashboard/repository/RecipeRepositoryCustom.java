package com.example.recipeapp.domain.dashboard.repository;

import com.example.recipeapp.domain.recipes.domain.model.Recipe;
import com.example.recipeapp.domain.recipes.domain.model.RecipeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;


public interface RecipeRepositoryCustom extends JpaRepository<Recipe, Long> {

    Optional<Recipe> findCategoryTopRecipeOrderByLikes(RecipeCategory category, LocalDateTime start, LocalDateTime end);

    Long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
