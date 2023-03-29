package com.project.comgle.sms.dto;

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

    String subject;

    String from;

    String content;

    List<MessageDto> messages;

    public static SmsRequestDto of(String type, String contentType, String countryCode,String subject,
                                   String from, String content, List<MessageDto> messages){

        return SmsRequestDto.builder()
                .type(type)
                .contentType(contentType)
                .countryCode(countryCode)
                .subject(subject)
                .from(from)
                .content(content)
                .messages(messages)
                .build();

    }
}
