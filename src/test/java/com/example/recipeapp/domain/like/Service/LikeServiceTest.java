package com.example.recipeapp.domain.like.Service;

import com.example.recipeapp.domain.like.domain.repository.LikeRepository;
import com.example.recipeapp.domain.recipes.domain.model.Recipe;
import com.example.recipeapp.domain.recipes.domain.repository.RecipeRepository;
import com.example.recipeapp.domain.user.domain.model.User;
import com.example.recipeapp.domain.user.domain.repository.UserRepository;
import com.example.recipeapp.domain.recipes.domain.model.RecipeCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class LikeServiceTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikeRepository likeRepository;

    private Recipe recipe;
    private List<User> users;

    @BeforeEach
    void setup(){
        // 테스트 전 좋아요 데이터 초기화
        likeRepository.deleteAll();
        recipeRepository.deleteAll();
        userRepository.deleteAll();

        // 레시피 생성
        User recipeWriter = userRepository.save(User.builder()
                .email("author@example.com")
                .username("author")
                .password("1234")
                .nickname("chef")
                .build());

        recipe = recipeRepository.save(Recipe.builder()
                .user(recipeWriter)
                .title("동시성 파스타")
                .content("테스트용 레시피입니다.")
                .category(RecipeCategory.WESTERN)
                .imageUrl("http://example.com/image.jpg")
                .build());

        // 유저 100명 생성
        users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = User.builder()
                    .email("user" + i + "@test.com")
                    .username("user" + i)
                    .password("pw" + i)
                    .nickname("nick" + i)
                    .build();
            users.add(userRepository.save(user));
        }

    }

    @Test
    void 동시에_100명이_좋아요를_누르면_좋아요수가_100이어야_한다() throws Exception {

        //동시에 실행할 스레드(=사용자) 수, 100명
        int threadCount = 100;

        //고정된 100개의 쓰레드 풀 생성
        //병렬 작업을 동시에 실행할 수 있게 해주는 Java concurrency API
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        //CountDownLatch는 100개의 작업이 모두 끝날 때까지 기다리게 하는 장치
        //각 스레드가 작업을 끝내면 .countDown() 호출, 0이 되면 .await() 중인 메인 스레드가 풀림
        CountDownLatch latch = new CountDownLatch(threadCount);

        //0~99까지 100명의 유저를 순회하며 각각 좋아요 요청을 보내는 스레드
        for (int i = 0; i < threadCount; i++) {
            final int userIndex = i;

            //좋아요 등록 작업을 비동기 스레드에서 실행
            executor.submit(() -> {

                //각각의 사용자(users.get(userIndex))가 같은 게시글(recipe.getId())에 좋아요를 누름
                try {
                    likeService.registerLike(users.get(userIndex).getId(), recipe.getId());
                } catch (Exception e) {
                    // 테스트의 목적 상 예외(중복 좋아요 or 에러)는 무시
                } finally {
                    latch.countDown();   //이 스레드의 작업이 끝났음을 알림. (CountDownLatch 카운트 1 감소)
                }
            });
        }

        //메인 스레드는 여기서 대기
        //100개의 스레드가 모두 작업을 마칠 때까지 기다림
        latch.await();

        // DB에서 다시 해당 게시글을 조회해서 최종 좋아요 수를 확인
        Recipe updatedRecipe = recipeRepository.findById(recipe.getId())
                .orElseThrow();

        System.out.println("최종 좋아요 수: " + updatedRecipe.getLikes());

        // 실제 좋아요 수가 100인지 검증.
        assertThat(updatedRecipe.getLikes()).isEqualTo(100);
    }

}