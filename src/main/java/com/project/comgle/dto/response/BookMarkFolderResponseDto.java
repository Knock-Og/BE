package com.project.comgle.dto.response;

import com.project.comgle.entity.BookMarkFolder;
import lombok.Getter;

@Getter
public class BookMarkFolderResponseDto {
    private String bookMarkFolderName;

    public BookMarkFolderResponseDto(BookMarkFolder bookMarkFolder){
        this.bookMarkFolderName = bookMarkFolder.getBookMarkFolderName();
    }
}
