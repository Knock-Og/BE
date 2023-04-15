package com.project.comgle.bookmark.dto;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class BookMarkFolderRequestDto {

    @Size(min = 1, max = 10, message = "폴더명은 1~10자 사이로 입력해주세요")
    @NotNull(message = "폴더명을 입력해주세요")
    @NotBlank(message = "폴더명을 입력해주세요")
    @Schema(description = SchemaDescriptionUtils.BookMarkForder.NAME, example = "폴더1")
    private String bookMarkFolderName;

}