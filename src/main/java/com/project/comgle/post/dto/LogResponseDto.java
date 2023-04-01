package com.project.comgle.post.dto;

import com.project.comgle.post.entity.Log;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class LogResponseDto {
    private String memberName;

    private String content;

    private String createDate;

    @Builder
    private LogResponseDto(String memberName, String content, String createDate) {
        this.memberName = memberName;
        this.content = content;
        this.createDate = createDate;
    }

    public static LogResponseDto from(Log log) {
        return LogResponseDto.builder()
                .memberName(log.getMemberName())
                .content(log.getContent())
                .createDate("편집한 시간 : " + log.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}