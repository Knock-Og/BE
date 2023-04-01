package com.project.comgle.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequestDto {

    String type;

    String contentType;

    String countryCode;

    String from;

    String content;

    List<SmsMessageDto> messages;

    public static SmsRequestDto of(String type, String contentType, String countryCode,
                                   String from, String content, List<SmsMessageDto> messages){

        return SmsRequestDto.builder()
                .type(type)
                .contentType(contentType)
                .countryCode(countryCode)
                .from(from)
                .content(content)
                .messages(messages)
                .build();

    }

}
