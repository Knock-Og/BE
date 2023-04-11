package com.project.comgle.admin.dto;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class SignupRequestDto {

    @NotNull(message = "Member name is required.")
    @Schema(description = SchemaDescriptionUtils.Member.NAME, example = "회원명" , maxLength = 20)
    private String memberName;

    @NotNull(message = "Email is required.")
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9\\-]+\\.)+([a-zA-Z]{2,5})$", message = "Please enter a valid email address to sign up.")
    @Schema(description = SchemaDescriptionUtils.EMAIL, example = "test@email.com" , maxLength = 50)
    private String email;

    @NotNull(message = "Password is required,")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).{8,32}$", message = "Passwords must be 8-32 letters long, including upper and lower cases or special characters.")
    @Schema(description = SchemaDescriptionUtils.Member.PASSWORD, example = "패스워드" ,minLength = 8, maxLength = 32)
    private String password;

    @NotNull(message = "Position is required.")
    @Schema(description = SchemaDescriptionUtils.Member.POSITION, example = "MEMBER",  allowableValues = {"MEMEBER", "MANAGER","OWNER"})
    private String position;

    @NotNull(message = "Phone number is requird.")
    @Schema(description = SchemaDescriptionUtils.Member.TEL, example = "010-xxxx-xxxx")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$" , message = "Please enter a valid phone number with a fixed format.")
    private String phoneNum;

}
