package com.project.comgle.dto.request;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class SignupRequestDto {

    @NotNull(message = "회원명은 필수 값입니다.")
    @Schema(description = SchemaDescriptionUtils.Member.NAME, example = "회원명" , maxLength = 20)
    private String memberName;

    @NotNull(message = "이메일은 필수 값입니다.")
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9\\-]+\\.)+([a-zA-Z]{2,5})$", message = "유효한 이메일 형식이 아닙니다.")
    @Schema(description = SchemaDescriptionUtils.EMAIL, example = "test@email.com" , maxLength = 50)
    private String email;

    @NotNull(message = "패스워드는 필수 값입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,32}$", message = "비밀번호는 대소문자, 숫자, 특수문자를 포함하여 8-32자 이내여야 합니다.")
    @Schema(description = SchemaDescriptionUtils.Member.PASSWORD, example = "패스워드" ,minLength = 8, maxLength = 32)
    private String password;

    @NotNull(message = "직책은 필수 값입니다.")
    @Schema(description = SchemaDescriptionUtils.Member.POSITION, example = "MEMBER",  allowableValues = {"MEMEBER", "MANAGER","OWNER"})
    private String position;

    @NotNull(message = "연락처는 필수 값입니다.")
    @Schema(description = SchemaDescriptionUtils.Member.TEL, example = "010-xxxx-xxxx")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$" , message = "올바르지 않은 연락처 형식입니다.")
    private String phoneNum;

}
