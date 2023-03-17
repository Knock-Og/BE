package com.project.comgle.dto.response;

import com.project.comgle.entity.BookMarkFolder;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BookMarkFolderResponseDto {
    private Long id;
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
