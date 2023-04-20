package com.project.comgle.bookmark.dto;

import com.project.comgle.global.config.swagger.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BookMarkFolderResponseDto {

    @Schema(description = SchemaDescriptionUtils.ID)
    private Long id;

    @Schema(description = SchemaDescriptionUtils.BookMarkForder.NAME , example = "폴더1")
    private String bookMarkFolderName;

    @Builder
    private BookMarkFolderResponseDto(Long id, String bookMarkFolderName) {
        this.id = id;
        this.bookMarkFolderName = bookMarkFolderName;
    }

    public static BookMarkFolderResponseDto of(Long id, String bookMarkFolderName){
        return BookMarkFolderResponseDto.builder()
                .id(id)
                .bookMarkFolderName(bookMarkFolderName)
                .build();
    }

}
