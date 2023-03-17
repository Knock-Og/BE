package com.project.comgle.controller;

import com.project.comgle.dto.request.CommentRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{post-id}")
public class CommentController {
    private final CommentService commentService;

    // 댓글등록
    @PostMapping("/comments")
    public ResponseEntity<MessageResponseDto> createComment(@PathVariable(name = "post-id") Long postId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequestDto, userDetails);
    }
}