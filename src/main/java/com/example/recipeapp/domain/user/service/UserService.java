package com.example.recipeapp.domain.user.service;

import com.example.recipeapp.domain.user.controller.dto.response.UserResponseDto;
import com.example.recipeapp.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAllByIsDeletedFalse(pageable).map(UserResponseDto::toDto);
    }

}
