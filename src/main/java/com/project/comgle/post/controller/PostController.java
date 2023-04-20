package com.project.comgle.post.controller;

import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.config.swagger.Message_200;
import com.project.comgle.global.config.swagger.Message_403;
import com.project.comgle.global.config.swagger.Message_404;
import com.project.comgle.post.dto.PostPageResponseDto;
import com.project.comgle.post.dto.PostRequestDto;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.post.dto.PostResponseDto;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.post.dto.PostSuccessResponseDto;
import com.project.comgle.post.service.PostService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "POST", description = "게시글 관련 API Document")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 작성 API", description = "게시글을 작성합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PostSuccessResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PostMapping("/post")
    public ResponseEntity<PostSuccessResponseDto> postAdd(@RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.addPost(postRequestDto, userDetails);
    }

    @Operation(summary = "게시글 삭제 API", description = "해당 게시글을 삭제합니다.",
        parameters = {
            @Parameter(name = "post-id", description = "게시글 고유번호", in = ParameterIn.PATH),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @DeleteMapping("/post/{post-id}")
    public ResponseEntity<MessageResponseDto> postDelete(@PathVariable("post-id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(id, userDetails.getMember());
    }

    @ExeTimer
    @Operation(summary = "게시글 수정 API", description = "해당 게시글을 수정합니다.",
        parameters = {
            @Parameter(name = "post-id", description = "게시글 고유번호", in = ParameterIn.PATH),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
            @ApiResponse(responseCode = "403", description = "403 에러", content = @Content(schema = @Schema(implementation = Message_403.class))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PutMapping("/post/{post-id}")
    public ResponseEntity<MessageResponseDto> postUpdate(@PathVariable("post-id") Long id, @RequestBody PostRequestDto postRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(id, postRequestDto, userDetails.getMember());
    }

    @ExeTimer
    @Operation(summary = "게시글 조회 API", description = "상세보기 PAGE를 위해 해당 게시글을 조회합니다.",
        parameters = {
            @Parameter(name = "post-id", description = "게시글 고유번호", in = ParameterIn.PATH),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PostResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "403 에러", content = @Content(schema = @Schema(implementation = Message_403.class))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @GetMapping("/post/{post-id}")
    public ResponseEntity<PostResponseDto> postRead(@PathVariable("post-id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.readPost(id, userDetails.getMember(), userDetails.getCompany());
    }

    @Operation(summary = "수정 상태 변경 API", description = "수정 완료 시 수정 가능하도록 상태를 변경합니다.",
        parameters = {
            @Parameter(name = "post-id", description = "게시글 고유번호", in = ParameterIn.PATH),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
            @ApiResponse(responseCode = "403", description = "403 에러", content = @Content(schema = @Schema(implementation = Message_403.class))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PutMapping("post/{post-id}/editingStatus")
    public ResponseEntity<MessageResponseDto> editingStatusChange(@PathVariable("post-id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.changeEditingStatus(id, userDetails.getMember());
    }
}
