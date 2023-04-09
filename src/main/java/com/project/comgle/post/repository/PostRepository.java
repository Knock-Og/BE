package com.project.comgle.post.repository;

import com.project.comgle.member.entity.Member;
import com.project.comgle.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long>{

    @Query("SELECT p FROM Post p WHERE p.valid = true and p.member = :member")
    Page<Post> findAllByMember(@Param("member") Member member, Pageable pageable);
    Optional<Post> findByIdAndMember(Long postId, Member member);
    int countByCategoryId(Long categoryId);
    int countByMember(Member member);
    Page<Post> findAllByCategoryId(Long categoryId, Pageable pageRequest);

}


