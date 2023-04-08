package com.project.comgle.post.repository;

import com.project.comgle.member.entity.Member;
import com.project.comgle.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findByIdAndMember(Long postId, Member member);

    List<Post> findAllByMember(Member member);

    Page<Post> findAllByMember(Member member, Pageable pageable);

    Set<Post> findAllByTitleContainsOrContentContaining(String title,String content);

    int countByCategoryId(Long categoryId);

    int countByMember(Member member);

    Page<Post> findAllByCategoryId(Long categoryId, Pageable pageRequest);

}