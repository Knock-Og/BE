package com.project.comgle.dto.response;

import com.project.comgle.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String memberName;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    private CommentResponseDto(Long id,
                              String memberName,
                              String comment,
                              LocalDateTime createdAt,
                              LocalDateTime modifiedAt) {
        this.id = id;
        this.memberName = memberName;
        this.comment = comment;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .memberName(comment.getMember().getMemberName())
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();
    }
}