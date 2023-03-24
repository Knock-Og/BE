package com.project.comgle.dto.response;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import com.project.comgle.entity.Log;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

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
                .createDate("편집한 시간 : " + log.getCreateDate())
                .build();
    }
}