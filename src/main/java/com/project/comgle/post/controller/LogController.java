package com.project.comgle.post.controller;

import com.project.comgle.post.dto.LogResponseDto;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.post.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "LOG", description = "Log 관련 API Document")
public class LogController {
    private final LogService logService;

    @Operation(summary = "Log 전체조회 API", description = "해당 게시글에 대한 로그 전체를 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/post/{post-id}/logs")
    public List<LogResponseDto> getAllLogs(@PathVariable(name = "post-id") Long postId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return logService.getAllLogs(postId, userDetails);
    }

}