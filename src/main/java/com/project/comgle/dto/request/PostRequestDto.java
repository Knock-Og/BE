package com.project.comgle.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    private String memberName;

    private String title;

    private String content;

    private String[] keywords;

    private String category;

    private String modifyPermission;

    private String readablePosition;

}
