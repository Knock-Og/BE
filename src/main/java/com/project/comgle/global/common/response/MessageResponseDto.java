package com.project.comgle.global.common.response;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageResponseDto {

    @Schema(description = SchemaDescriptionUtils.STATUS_CODE, example = "200")
    private int status;

    @Schema(description = SchemaDescriptionUtils.MESSAGE,example = "성공")
    private String message;

    @Builder
    private MessageResponseDto(int status, String message){
        this.status = status;
        this.message = message;
    }

    public static MessageResponseDto of(int status, String message){
        return MessageResponseDto.builder()
                .status(status)
                .message(message)
                .build();
    }

}
