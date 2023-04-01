package com.project.comgle.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessageDto {

    String to;
    String content;

    public static SmsMessageDto of(String to, String content){
        return SmsMessageDto.builder()
                .to(to)
                .content(content)
                .build();
    }

}
