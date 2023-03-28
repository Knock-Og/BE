package com.project.comgle.sms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    String to;

    String content;

    public static MessageDto of(String to, String content){
        return MessageDto.builder()
                .to(to)
                .content(content)
                .build();
    }

}
