package com.project.comgle.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SignupRequestDto {
    @NotNull(message = "이름은 필수 값입니다.")
    private String memberName;

    @NotNull(message = "아이디는 필수 값입니다.")
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9\\-]+\\.)+([a-zA-Z]{2,5})$", message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "비밀번호는 필수 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,32}$", message = "비밀번호는 대소문자, 숫자, 특수문자를 포함하여 8-32자 이내여야 합니다.")
    private String password;

    @NotNull(message = "직책은 필수 값입니다.")
    private String position;

}
