package com.project.comgle.mypage.controller;

import com.project.comgle.bookmark.dto.PostPageResponseDto;
import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.mypage.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "MYPAGE", description = "내 게시글 관련 API Document")
public class MyPageController {

    private final MyPageService myPageService;

    @ExeTimer
    @Operation(summary = "내 게시글 전체조회 API", description = "내가 작성한 게시글 전체를 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/mypage/posts")
    public PostPageResponseDto getAllMyPosts(@RequestParam("p") int page, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.getAllMyPosts(page, userDetails);
    }

}