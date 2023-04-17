package com.project.comgle.post.dto;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
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
    private Long id;

    @Builder
    private PostSuccessResponseDto(int status, String message, Long id){
        this.status = status;
        this.message = message;
        this.id = id;
    }

    public static PostSuccessResponseDto of(int status, String message, Long id){
        return PostSuccessResponseDto.builder()
                .status(status)
                .message(message)
                .id(id)
                .build();
    }

}
