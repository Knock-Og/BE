package com.project.comgle.repository;

import com.project.comgle.entity.Comment;
import com.project.comgle.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByPostOrderByCreatedAtDesc(Post post);

    void deleteAllByPost(Post post);
}