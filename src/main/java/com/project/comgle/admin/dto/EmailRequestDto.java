package com.project.comgle.admin.dto;

import com.project.comgle.global.config.swagger.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class EmailRequestDto {

    @NotNull(message = "Email is required.")
    @Pattern(regexp = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9\\-]+\\.)+([a-zA-Z]{2,5})$", message = "Please enter a valid email address to sign up.")
    @Schema(description = SchemaDescriptionUtils.EMAIL, example = "test@email.com" , maxLength = 50)
    private String email;
}
