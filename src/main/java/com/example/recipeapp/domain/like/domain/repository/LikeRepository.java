package com.example.recipeapp.domain.like.domain.repository;

import com.example.recipeapp.domain.like.domain.model.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {

    //특정 사용자가 특정 게시글(Post)에 대해 좋아요를 눌렀는지 조회 (중복 등록 확인, 취소 시 존재 여부 확인)
    Optional<Likes> findByUserAndPost(User user, Post post);

    //특정 게시글에 대한 총 좋아요 수
    long countByPost(Post post);

    //좋아요 삭제
    void deleteByUserAndPost(User user, Post post);

}
