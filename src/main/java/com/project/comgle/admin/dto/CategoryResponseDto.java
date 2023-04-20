package com.project.comgle.admin.dto;

import com.project.comgle.global.config.swagger.SchemaDescriptionUtils;
import com.project.comgle.admin.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponseDto {

    @Schema(description = SchemaDescriptionUtils.ID)
    private Long id;

    @Schema(description = SchemaDescriptionUtils.Category.NAME)
    private String categoryName;

    @Builder
    private CategoryResponseDto(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public static CategoryResponseDto from(Category category){
        return CategoryResponseDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
    }

}
