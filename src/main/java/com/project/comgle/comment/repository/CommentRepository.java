package com.project.comgle.comment.repository;

import com.project.comgle.comment.entity.Comment;
import com.project.comgle.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByPostOrderByCreatedAtDesc(Post post);
    void deleteAllByPost(Post post);

}