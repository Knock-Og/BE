package com.project.comgle.dto.request;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class BookMarkFolderRequestDto {

    @Size(min = 1, max = 100)
    @Schema(description = SchemaDescriptionUtils.BookMarkForder.NAME, example = "폴더1")
    private String bookMarkFolderName;
}