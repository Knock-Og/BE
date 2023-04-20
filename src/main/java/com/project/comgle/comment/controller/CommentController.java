package com.project.comgle.comment.controller;

import com.project.comgle.comment.dto.CommentRequestDto;
import com.project.comgle.comment.dto.CommentResponseDto;
import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.global.config.swagger.Message_201;
import com.project.comgle.global.config.swagger.Message_403;
import com.project.comgle.global.config.swagger.Message_404;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.comment.service.CommentService;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post/{post-id}")
@Tag(name = "COMMENT", description = "댓글 관련 API Document")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 등록 API", description = "해당 게시글에 댓글을 등록합니다.",
        parameters = @Parameter(name = "post-id", description = "게시글 고유번호", in = ParameterIn.PATH),
        responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_201.class))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PostMapping("/comment")
    public ResponseEntity<MessageResponseDto> createComment(@PathVariable(name = "post-id") Long postId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return commentService.createComment(postId, commentRequestDto, userDetails);
    }

    @ExeTimer
    @Operation(summary = "댓글 모두 조회 API", description = "해당 게시글에 댓글을 모두 조회합니다.",
        parameters = @Parameter(name = "post-id", description = "게시글 고유번호", in = ParameterIn.PATH),
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content( array = @ArraySchema( schema = @Schema(implementation = CommentResponseDto.class)))),
                @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @GetMapping("/comments")
    public List<CommentResponseDto> getComments(@PathVariable(name = "post-id") Long postId) {

        return commentService.getComments(postId);
    }

    @Operation(summary = "댓글 수정 API", description = "해당 게시글에 해당 댓글을 수정합니다.",
        parameters = @Parameter(name = "comment-id", description = "댓글 고유번호", in = ParameterIn.PATH),
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content( array = @ArraySchema( schema = @Schema(implementation = CommentResponseDto.class)))),
                @ApiResponse(responseCode = "403", description = "403 에러", content = @Content(schema = @Schema(implementation = Message_403.class))),
                @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PutMapping("/comment/{comment-id}")
    public ResponseEntity<MessageResponseDto> updateComment(@PathVariable(name = "post-id") Long postId,
                                                            @PathVariable(name = "comment-id") Long commentId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return commentService.updateComment(postId, commentId, commentRequestDto, userDetails);
    }

    @Operation(summary = "댓글 삭제 API", description = "해당 게시글에 해당 댓글을 삭제합니다.",
        parameters = @Parameter(name = "comment-id", description = "댓글 고유번호", in = ParameterIn.PATH),
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content( array = @ArraySchema( schema = @Schema(implementation = CommentResponseDto.class)))),
                @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @DeleteMapping("/comment/{comment-id}")
    public ResponseEntity<MessageResponseDto> deleteComment(@PathVariable(name = "post-id") Long postId,
                                                            @PathVariable(name = "comment-id") Long commentId,
                                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return commentService.deleteComment(postId, commentId, userDetails.getMember());
    }

}