package com.project.comgle.post.controller;

import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.config.swagger.Message_200;
import com.project.comgle.global.config.swagger.Message_404;
import com.project.comgle.post.dto.LogResponseDto;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.post.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @ExeTimer
    @Operation(summary = "Log 전체조회 API", description = "해당 게시글에 대한 로그 전체를 조회합니다.",
        parameters = @Parameter(name = "post-id", description = "게시글 고유번호", in = ParameterIn.PATH),
        responses ={
            @ApiResponse(responseCode = "200", description = "성공", content = @Content( array = @ArraySchema( schema = @Schema(implementation = LogResponseDto.class)))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @GetMapping("/post/{post-id}/logs")
    public List<LogResponseDto> logList(@PathVariable(name = "post-id") Long postId,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return logService.listLog(postId, userDetails);
    }
}