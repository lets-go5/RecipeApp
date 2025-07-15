package com.example.recipeapp.domain.user.service;

import com.example.recipeapp.domain.user.controller.dto.response.UserResponseDto;
import com.example.recipeapp.domain.user.domain.model.User;
import com.example.recipeapp.domain.user.domain.model.UserRoleEnum;
import com.example.recipeapp.domain.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("getAllUsers는 isDeleted=false 유저만 페이지로 반환한다")
    void getAllUsers_returnsActiveUsers() {
        // given
        userRepository.save(User.builder()
                .username("user1")
                .nickname("닉네임1")
                .email("user1@test.com")
                .password("pw1")
                .role(UserRoleEnum.USER)
                .isDeleted(false)
                .build());

        userRepository.save(User.builder()
                .username("user2")
                .nickname("닉네임2")
                .email("user2@test.com")
                .password("pw2")
                .role(UserRoleEnum.USER)
                .isDeleted(false)
                .build());

        userRepository.save(User.builder()
                .username("탈퇴회원")
                .nickname("닉네임3")
                .email("user3@test.com")
                .password("pw3")
                .role(UserRoleEnum.USER)
                .isDeleted(true)   // 이건 안 나와야 함
                .build());

        PageRequest pageable = PageRequest.of(0, 10);

        // when
        Page<UserResponseDto> page = userService.getAllUsers(pageable);

        // then
        assertThat(page.getTotalElements()).isEqualTo(2);  // 삭제 안된 회원만 조회
        assertThat(page.getContent())
                .extracting(UserResponseDto::getUsername)
                .contains("user1", "user2")
                .doesNotContain("탈퇴회원");
    }
}