package com.example.recipeapp.domain.user.domain.repository;

import com.example.recipeapp.domain.user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);

    Page<User> findAllByIsDeletedFalse(Pageable pageable);
}
