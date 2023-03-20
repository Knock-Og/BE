package com.project.comgle.dto.request;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @Schema(description = SchemaDescriptionUtils.Comment.COMMENT, example = "댓글" , maxLength = 255)
    private String comment;
}