package com.project.comgle.dto.request;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
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

    @NotNull(message = "회원명은 필수 값입니다.")
    @Schema(description = SchemaDescriptionUtils.Member.NAME, example = "회원명" , maxLength = 20)
    private String memberName;

    @NotNull(message = "연락처는 필수 값입니다.")
    @Schema(description = SchemaDescriptionUtils.Member.TEL, example = "010-xxxx-xxxx")
    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$" , message = "올바르지 않은 연락처 형식입니다.")
    private String phoneNum;

    @NotNull(message = "인증코드가 없습니다.")
    @Schema(description = SchemaDescriptionUtils.SMS.AUTHENTICATION_CODE, example = "xxxxxx")
    private int authenticationCode;

}
