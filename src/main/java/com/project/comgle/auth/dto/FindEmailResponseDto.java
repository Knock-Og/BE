package com.project.comgle.auth.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindEmailResponseDto {

    private String requestId;

    private LocalDateTime requestTime;

    private String statusCode;

    private String statusName;

    @Setter
    private String message;

    @Setter
    private String phoneNum;

    public static FindEmailResponseDto of(String requestId, LocalDateTime requestTime, String statusCode, String statusName){
        return FindEmailResponseDto.builder()
                .requestId(requestId)
                .requestTime(requestTime)
                .statusCode(statusCode)
                .statusName(statusName)
                .build();
    }

}
