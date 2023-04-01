package com.project.comgle.bookmark.dto;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class BookMarkFolderRequestDto {

    @Size(min = 1, max = 100)
    @Schema(description = SchemaDescriptionUtils.BookMarkForder.NAME, example = "폴더1")
    private String bookMarkFolderName;

}