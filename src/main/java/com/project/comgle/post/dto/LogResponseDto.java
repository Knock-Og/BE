package com.project.comgle.post.dto;

import com.project.comgle.post.entity.Log;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class LogResponseDto {
    private String memberName;

    private String content;

    private String oldContent;

    private  String newContent;

    private List<Integer> changedLineNum;

    private String createDate;

    @Builder
    private LogResponseDto(String memberName, String content, String oldContent, String newContent, List<Integer> changedLineNum, String createDate) {
        this.memberName = memberName;
        this.content = content;
        this.oldContent = oldContent;
        this.newContent = newContent;
        this.changedLineNum = changedLineNum;
        this.createDate = createDate;
    }

    public static LogResponseDto from(Log log) {
        return LogResponseDto.builder()
                .memberName(log.getMemberName())
                .content(log.getContent())
                .oldContent(log.getOldContent())
                .newContent(log.getNewContent())
                .changedLineNum(log.getChangedLineNum())
                .createDate("편집한 시간 : " + log.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}