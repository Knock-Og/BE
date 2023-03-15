package com.project.comgle.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class BookMarkFolderRequestDto {
    @Size(min = 1, max = 100)
    private String bookMarkFolderName;
}