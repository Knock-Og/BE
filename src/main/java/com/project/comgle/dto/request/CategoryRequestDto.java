package com.project.comgle.dto.request;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CategoryRequestDto {

    @NotBlank(message = "빈 칸입니다.")
    @Schema(description = SchemaDescriptionUtils.Category.NAME, example = "공지사항" , maxLength = 20)
    private String categoryName;

}
