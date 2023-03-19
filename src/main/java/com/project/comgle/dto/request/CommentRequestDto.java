package com.project.comgle.dto.request;

import com.project.comgle.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String comment;
}