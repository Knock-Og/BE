package com.project.comgle.dto.response;

import com.project.comgle.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponseDto {

    private Long id;
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
