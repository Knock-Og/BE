package com.project.comgle.global.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MessageResponseDto {

    private int status;

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
