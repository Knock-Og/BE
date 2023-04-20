package com.project.comgle.post.controller;

import com.project.comgle.global.config.swagger.Message_200;
import com.project.comgle.global.config.swagger.Message_404;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.post.service.SseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Tag(name = "POST", description = "게시글 관련 API Document")
public class SseController {
    private final SseService sseService;

    @Operation(summary = "SSE API", description = "수정상태인 게시글에 대해서 완료 알림을 위한 SSE 통신하는 기능입니다",
            parameters = {
                @Parameter(name = "post-id", description = "게시글 고유번호", in = ParameterIn.PATH),
            }, responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SseEmitter.class))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
    }
    )
    @GetMapping(value = "/connect/{post-id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subScribe(@PathVariable("post-id") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return sseService.subscribe(postId, userDetails.getMember().getId());
    }
}
