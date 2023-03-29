package com.project.comgle.dto.request;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Data
public class EmailAuthRequestDto{
    @NotEmpty(message = "이메일을 입력해주세요")
    @Email
    @Schema(description = SchemaDescriptionUtils.EMAIL, example = "이메일")
    public String email;
}
