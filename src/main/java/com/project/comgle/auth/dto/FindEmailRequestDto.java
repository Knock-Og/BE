package com.project.comgle.auth.dto;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindEmailRequestDto {

    @NotNull(message = "Member name is required.")
    @Schema(description = SchemaDescriptionUtils.Member.NAME, example = "회원명" , maxLength = 20)
    private String memberName;

    @NotNull(message = "Phone number is required.")
    @Schema(description = SchemaDescriptionUtils.Member.TEL, example = "010-xxxx-xxxx")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$" , message = "올바르지 않은 연락처 형식입니다.")
    private String phoneNum;

    @NotNull(message = "Authentication code does not exist.")
    @Schema(description = SchemaDescriptionUtils.SMS.AUTHENTICATION_CODE, example = "xxxxxx")
    private int authenticationCode;

}
