package com.project.comgle.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class CategoryRequestDto {

    @NotBlank(message = "빈 칸입니다.")
    private String categoryName;

}
