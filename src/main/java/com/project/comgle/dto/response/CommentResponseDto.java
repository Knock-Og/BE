package com.project.comgle.dto.response;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import com.project.comgle.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class CommentResponseDto {

    @Schema(description = SchemaDescriptionUtils.ID)
    private Long id;

    @Schema(description = SchemaDescriptionUtils.Member.NAME , example = "user")
    private String memberName;

    @Schema(description = SchemaDescriptionUtils.Comment.COMMENT, example = "댓글내용")
    private String comment;

    @Schema(description = SchemaDescriptionUtils.CREATE_AT)
    private LocalDateTime createdAt;

    @Schema(description = SchemaDescriptionUtils.MODIFIED_AT)
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