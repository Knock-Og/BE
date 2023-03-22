package com.project.comgle.service;

import com.project.comgle.dto.request.CommentRequestDto;
import com.project.comgle.dto.response.CommentResponseDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.Comment;
import com.project.comgle.entity.Post;
import com.project.comgle.exception.CustomException;
import com.project.comgle.exception.ExceptionEnum;
import com.project.comgle.repository.CommentRepository;
import com.project.comgle.repository.PostRepository;
import com.project.comgle.security.UserDetailsImpl;
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

    // 댓글등록
    public ResponseEntity<MessageResponseDto> createComment(Long postId, CommentRequestDto commentRequestDto, UserDetailsImpl userDetails) {
        Optional<Post> findPost = postRepository.findById(postId);

        if (findPost.isEmpty()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        // 댓글생성
        Comment comment = Comment.builder()
                .comment(commentRequestDto.getComment())
                .member(userDetails.getMember())
                .post(findPost.get())
                .build();

        // 댓글 DB 저장
        commentRepository.save(comment);

        return ResponseEntity.ok(MessageResponseDto.of(HttpStatus.OK.value(), "Add Comment Successfully"));
    }

    // 댓글조회
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

    // 댓글수정
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

    // 댓글삭제
    public ResponseEntity<MessageResponseDto> deleteComment(Long postId, Long commentId, UserDetailsImpl userDetails) {
        Optional<Comment> findComment = commentRepository.findById(commentId);

        Optional<Post> findPost = postRepository.findById(postId);

        if (findComment.isEmpty()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_COMMENT);
        }
        if (findPost.isEmpty()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        // 댓글 DB 삭제
        commentRepository.delete(findComment.get());

        return ResponseEntity.ok(MessageResponseDto.of(HttpStatus.OK.value(), "Delete Comment Successfully"));
    }
}