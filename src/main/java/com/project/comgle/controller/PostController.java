package com.project.comgle.controller;

import com.project.comgle.dto.request.PostRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.dto.response.PostResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.PostService;
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
    @PostMapping("/posts")
    public ResponseEntity<MessageResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails);
    }

    @Operation(summary = "게시글 삭제 API", description = "해당 게시글을 삭제합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/post/{post-id}")
    public ResponseEntity<MessageResponseDto> deletePost(@PathVariable("post-id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getMember());
    }

    @Operation(summary = "게시글 수정 API", description = "해당 게시글을 수정합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/post/{post-id}")
    public ResponseEntity<MessageResponseDto> updatePost(@PathVariable("post-id") Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, postRequestDto, userDetails.getMember());
    }

    @Operation(summary = "게시글 조회 API", description = "상세보기 PAGE를 위해 해당 게시글을 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/post/{post-id}")
    public ResponseEntity<PostResponseDto> readPost(@PathVariable("post-id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.readPost(id, userDetails.getMember());
    }
}
