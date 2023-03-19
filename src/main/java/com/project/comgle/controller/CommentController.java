package com.project.comgle.controller;

import com.project.comgle.dto.request.CommentRequestDto;
import com.project.comgle.dto.response.CommentResponseDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/{post-id}")
public class CommentController {
    private final CommentService commentService;

    // 댓글등록
    @PostMapping("/comment")
    public ResponseEntity<MessageResponseDto> createComment(@PathVariable(name = "post-id") Long postId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequestDto, userDetails);
    }

    // 댓글조회
    @GetMapping("/comments")
    public List<CommentResponseDto> getComments(@PathVariable(name = "post-id") Long postId) {
        return commentService.getComments(postId);
    }

    // 댓글삭제
    @DeleteMapping("/comment/{comment-id}")
    public ResponseEntity<MessageResponseDto> deleteComment(@PathVariable(name = "post-id") Long postId,
                                                            @PathVariable(name = "comment-id") Long commentId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(postId, commentId, userDetails);
    }
}