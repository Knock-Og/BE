package com.project.comgle.repository;

import com.project.comgle.entity.Member;
import com.project.comgle.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post,Long> {

    Optional<Post> findByIdAndMember(Long postId, Member member);

    List<Post> findAllByCategoryId(Long categoryId);

    List<Post> findAllByMember(Member member);

    Set<Post> findAllByTitleContainsOrContentContaining(String title,String content);

}
