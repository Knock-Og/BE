package com.project.comgle.admin.dto;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class PasswordCheckRequestDto {

    @NotNull(message = "패스워드는 필수 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,32}$", message = "Password must be 8-32 characters long, including case, number, and special characters.")
    @Schema(description = SchemaDescriptionUtils.Member.PASSWORD, example = "패스워드" ,minLength = 8, maxLength = 32)
    private String password;
}
