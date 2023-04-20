package com.project.comgle.comment.dto;

import com.project.comgle.global.config.swagger.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    @Schema(description = SchemaDescriptionUtils.Comment.COMMENT, example = "댓글" , maxLength = 255)
    private String comment;

}