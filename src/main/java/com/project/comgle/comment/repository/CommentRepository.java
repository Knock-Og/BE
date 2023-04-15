package com.project.comgle.comment.repository;

import com.project.comgle.comment.entity.Comment;
import com.project.comgle.member.entity.Member;
import com.project.comgle.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByPostOrderByModifiedAt(Post post);
    Optional<Comment> findByIdAndMember(Long id, Member member);
}