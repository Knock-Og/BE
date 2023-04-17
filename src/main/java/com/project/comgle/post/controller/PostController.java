package com.project.comgle.post.controller;

import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.post.dto.PostRequestDto;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.post.dto.PostResponseDto;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.post.dto.PostSuccessResponseDto;
import com.project.comgle.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "POST", description = "게시글 관련 API Document")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 작성 API", description = "게시글을 작성합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/post")
    public ResponseEntity<PostSuccessResponseDto> postAdd(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.addPost(postRequestDto, userDetails);
    }

    @Operation(summary = "게시글 삭제 API", description = "해당 게시글을 삭제합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/post/{post-id}")
    public ResponseEntity<MessageResponseDto> postDelete(@PathVariable("post-id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getMember());
    }

    @ExeTimer
    @Operation(summary = "게시글 수정 API", description = "해당 게시글을 수정합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/post/{post-id}")
    public ResponseEntity<MessageResponseDto> postUpdate(@PathVariable("post-id") Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, postRequestDto, userDetails.getMember());
    }

    @ExeTimer
    @Operation(summary = "게시글 조회 API", description = "상세보기 PAGE를 위해 해당 게시글을 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/post/{post-id}")
    public ResponseEntity<PostResponseDto> postRead(@PathVariable("post-id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.readPost(id, userDetails.getMember(), userDetails.getCompany());
    }

    // 임시로 editingStatus 수정을 위한 API
    @PutMapping("post/{post-id}/editingStatus")
    public ResponseEntity<MessageResponseDto> editingStatusChange(@PathVariable("post-id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.changeEditingStatus(id, userDetails.getMember());
    }
}
