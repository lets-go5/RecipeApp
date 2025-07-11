package com.example.recipeapp.domain.recipes.controller;

import com.example.recipeapp.domain.recipes.controller.dto.RecipeSummaryResponse;
import com.example.recipeapp.domain.recipes.domain.model.Recipe;
import com.example.recipeapp.domain.recipes.domain.repository.RecipeRepository;
import com.example.recipeapp.global.exception.CustomException;
import com.example.recipeapp.global.exception.ErrorCode;
import com.example.recipeapp.global.response.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.recipeapp.domain.auth.domain.model.AuthUser;
import com.example.recipeapp.domain.recipes.controller.dto.RecipeCreateRequest;
import com.example.recipeapp.domain.recipes.controller.dto.RecipeResponse;
import com.example.recipeapp.domain.recipes.controller.dto.RecipeUpdateRequest;
import com.example.recipeapp.domain.recipes.service.RecipeService;
import com.example.recipeapp.domain.user.domain.model.User;
import com.example.recipeapp.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;

    // 1. 레시피 작성
    @PostMapping
    public ApiResponse<RecipeResponse> createRecipe(
            @RequestBody RecipeCreateRequest request,
            @AuthenticationPrincipal AuthUser authUser) {

        User user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        RecipeResponse response = recipeService.createRecipe(request, user);
        return ApiResponse.success("레시피 등록이 완료되었습니다.", response);
    }

    // 2. 전체 레시피 조회
    @GetMapping
    public ApiResponse<List<RecipeResponse>> getAllRecipes() {
        List<RecipeResponse> allRecipes = recipeService.getAllRecipes();
        return ApiResponse.success("전체 레시피 목록입니다.", allRecipes);
    }

    // 3. 단일 레시피 조회
    @GetMapping("/{id}")
    public ApiResponse<RecipeResponse> getRecipe(@PathVariable Long id) {
        RecipeResponse recipe = recipeService.getRecipeById(id);
        return ApiResponse.success("레시피 상세 정보입니다.", recipe);
    }

    // 4. 레시피 수정
    @PatchMapping("/{id}")
    public ApiResponse<Void> updateRecipe(@PathVariable Long id, @RequestBody RecipeUpdateRequest request) {
        recipeService.updateRecipe(id, request);
        return ApiResponse.success("레시피가 수정되었습니다.", null);
    }

    // 5. 레시피 삭제
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ApiResponse.success("레시피가 삭제되었습니다.", null);
    }

    // 6. 오늘 등록된 신규 레시피 조회
    @GetMapping("/today")
    public ApiResponse<List<RecipeResponse>> getTodayRecipes() {
        List<RecipeResponse> todayRecipes = recipeService.getTodayRecipes();
        return ApiResponse.success("오늘 등록된 신규 레시피 목록입니다.", todayRecipes);
    }

    // 7. 오늘의 인기 레시피 (카테고리별)
    @GetMapping("/today/popular")
    public ApiResponse<RecipeResponse> getTodayPopularRecipeByCategory(@RequestParam String category) {
        RecipeResponse recipe = recipeService.getTodayPopularRecipeByCategory(category);
        return ApiResponse.success("오늘의 인기 레시피입니다.", recipe);
    }
}