package com.project.comgle.post.dto;

import com.project.comgle.global.config.swagger.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostSuccessResponseDto {
    @Schema(description = SchemaDescriptionUtils.STATUS_CODE, example = "200")
    private int status;

    @Schema(description = SchemaDescriptionUtils.MESSAGE,example = "성공")
    private String message;

    @Schema(description = SchemaDescriptionUtils.ID )
    private Long postId;

    @Builder
    private PostSuccessResponseDto(int status, String message, Long postId){
        this.status = status;
        this.message = message;
        this.postId = postId;
    }

    public static PostSuccessResponseDto of(int status, String message, Long postId){
        return PostSuccessResponseDto.builder()
                .status(status)
                .message(message)
                .postId(postId)
                .build();
    }

}
