package com.project.comgle.controller;

import com.project.comgle.dto.request.PostRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.dto.response.PostResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public ResponseEntity<MessageResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails);
    }

    @DeleteMapping("/post/{post-id}")
    public ResponseEntity<MessageResponseDto> deletePost(@PathVariable("post-id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getMember());
    }

    @PutMapping("/post/{post-id}")
    public ResponseEntity<MessageResponseDto> updatePost(@PathVariable("post-id") Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, postRequestDto, userDetails.getUser());
    }

    @GetMapping("/post/{post-id}")
    public ResponseEntity<PostResponseDto> readPost(@PathVariable("post-id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.readPost(id, userDetails.getMember());
    }
}
