package com.project.comgle.dto.request;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    @Schema(description = SchemaDescriptionUtils.Post.TITLE, maxLength = 255 , example = "제목입니다.")
    private String title;

    @Schema(description = SchemaDescriptionUtils.Post.CONTENT,  example = "내용입니다.")
    private String content;

    @Schema(description = SchemaDescriptionUtils.Keyword.NAME)
    private String[] keywords;

    @Schema(description = SchemaDescriptionUtils.Category.NAME,  example = "공지사항")
    private String category;

    @Schema(description = SchemaDescriptionUtils.Post.MEDIFY_PERMISSION,
            example = "MEMBER" ,allowableValues = {"MEMEBER", "MANAGER","OWNER"})
    private String modifyPermission;

    @Schema(description = SchemaDescriptionUtils.Post.READABLE_POSITION,
            example = "MEMBER",allowableValues = {"MEMEBER", "MANAGER","OWNER"})
    private String readablePosition;

}
