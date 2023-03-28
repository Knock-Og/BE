package com.project.comgle.sms.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsResponseDto {

    private String requestId;

    private LocalDateTime requestTime;

    private String statusCode;

    private String statusName;

    @Setter
    private String phoneNum;

    public static SmsResponseDto of(String requestId, LocalDateTime requestTime, String statusCode, String statusName){
        return SmsResponseDto.builder()
                .requestId(requestId)
                .requestTime(requestTime)
                .statusCode(statusCode)
                .statusName(statusName)
                .build();
    }


}
