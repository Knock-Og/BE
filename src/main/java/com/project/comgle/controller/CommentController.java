package com.project.comgle.controller;

import com.project.comgle.dto.request.CommentRequestDto;
import com.project.comgle.dto.response.CommentResponseDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/{post-id}")
@Tag(name = "COMMENT", description = "댓글 관련 API Document")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 등록 API", description = "해당 게시글에 댓글을 등록합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/comment")
    public ResponseEntity<MessageResponseDto> createComment(@PathVariable(name = "post-id") Long postId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequestDto, userDetails);
    }

    @Operation(summary = "댓글 모두 조회 API", description = "해당 게시글에 댓글을 모두 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/comments")
    public List<CommentResponseDto> getComments(@PathVariable(name = "post-id") Long postId) {
        return commentService.getComments(postId);
    }

    @Operation(summary = "댓글 수정 API", description = "해당 게시글에 해당 댓글을 수정합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/comment/{comment-id}")
    public ResponseEntity<MessageResponseDto> updateComment(@PathVariable(name = "post-id") Long postId,
                                                            @PathVariable(name = "comment-id") Long commentId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(postId, commentId, commentRequestDto, userDetails);
    }

    @Operation(summary = "댓글 삭제 API", description = "해당 게시글에 해당 댓글을 삭제합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/comment/{comment-id}")
    public ResponseEntity<MessageResponseDto> deleteComment(@PathVariable(name = "post-id") Long postId,
                                                            @PathVariable(name = "comment-id") Long commentId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(postId, commentId, userDetails);
    }
}