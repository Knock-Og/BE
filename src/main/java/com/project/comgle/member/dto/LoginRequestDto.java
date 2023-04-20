package com.project.comgle.member.dto;

import com.project.comgle.global.config.swagger.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @Schema(description = SchemaDescriptionUtils.EMAIL, example = "test@email.com" , maxLength = 50)
    private String email;

    @Schema(description = SchemaDescriptionUtils.Member.PASSWORD, example = "#test123" , minLength = 8, maxLength = 32)
    private String password;

}
