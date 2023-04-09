package com.project.comgle.member.dto;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class PasswordRequestDto {

    @NotNull(message = "패스워드는 필수 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,32}$", message = "비밀번호는 대소문자, 숫자, 특수문자를 포함하여 8-32자 이내여야 합니다.")
    @Schema(description = SchemaDescriptionUtils.Member.PASSWORD, example = "패스워드" ,minLength = 8, maxLength = 32)
    private String password;

}
