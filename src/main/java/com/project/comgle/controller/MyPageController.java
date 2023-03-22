package com.project.comgle.controller;

import com.project.comgle.dto.response.PostResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "MYPAGE", description = "내 게시글 관련 API Document")
public class MyPageController {
    private final MyPageService myPageService;

    @Operation(summary = "내 게시글 전체조회 API", description = "내가 작성한 게시글 전체를 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/mypage/posts")
    public List<PostResponseDto> getAllMyPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getAllMyPosts(userDetails);
    }
}