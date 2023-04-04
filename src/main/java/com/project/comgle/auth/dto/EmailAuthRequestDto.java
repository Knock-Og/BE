package com.project.comgle.auth.dto;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class EmailAuthRequestDto{

    @NotEmpty(message = "이메일을 입력해주세요")
    @Email
    @Schema(description = SchemaDescriptionUtils.EMAIL, example = "이메일")
    private String email;

    @NotNull(message = "인증코드가 없습니다.")
    @Schema(description = SchemaDescriptionUtils.SMS.AUTHENTICATION_CODE, example = "xxxxxx")
    private String authenticationCode;

}
