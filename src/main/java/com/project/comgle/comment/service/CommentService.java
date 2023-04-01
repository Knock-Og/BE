package com.project.comgle.comment.service;

import com.project.comgle.comment.dto.CommentRequestDto;
import com.project.comgle.comment.dto.CommentResponseDto;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.comment.entity.Comment;
import com.project.comgle.post.entity.Post;
import com.project.comgle.post.entity.WeightEnum;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.comment.repository.CommentRepository;
import com.project.comgle.post.repository.PostRepository;
import com.project.comgle.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public ResponseEntity<MessageResponseDto> createComment(Long postId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        Optional<Post> findPost = postRepository.findById(postId);

        if (findPost.isEmpty()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        Comment comment = Comment.of(commentRequestDto, userDetails.getMember(), findPost.get());

        commentRepository.save(comment);

        findPost.get().updateMethod(WeightEnum.COMMENTCOUNT.getNum());

        return ResponseEntity.ok(MessageResponseDto.of(HttpStatus.OK.value(), "Add Comment Successfully"));
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(Long postId) {

        Optional<Post> getPost = postRepository.findById(postId);

        if (getPost.isEmpty()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        List<Comment> getComments = commentRepository.findAllByPostOrderByCreatedAtDesc(getPost.get());

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment comment : getComments) {
            commentResponseDtos.add(CommentResponseDto.from(comment));
        }

        return commentResponseDtos;
    }


    public ResponseEntity<MessageResponseDto> updateComment(Long postId, Long commentId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {

        Optional<Post> getPost = postRepository.findById(postId);
        if (getPost.isEmpty()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        Optional<Comment> getComment = commentRepository.findById(commentId);
        if (getComment.isEmpty()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_COMMENT);
        }

        if (!getComment.get().getMember().getId().equals(userDetails.getMember().getId())) {
            throw new CustomException(ExceptionEnum.INVALID_PERMISSION_TO_MODIFY);
        }

        getComment.get().updateComment(commentRequestDto);

        return ResponseEntity.ok(MessageResponseDto.of(HttpStatus.OK.value(), "Update Comment Successfully"));
    }

    public ResponseEntity<MessageResponseDto> deleteComment(Long postId, Long commentId, UserDetailsImpl userDetails) {

        Optional<Comment> findComment = commentRepository.findById(commentId);

        Optional<Post> findPost = postRepository.findById(postId);

        if (findComment.isEmpty()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_COMMENT);
        }
        if (findPost.isEmpty()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        commentRepository.delete(findComment.get());

        return ResponseEntity.ok(MessageResponseDto.of(HttpStatus.OK.value(), "Delete Comment Successfully"));
    }

}