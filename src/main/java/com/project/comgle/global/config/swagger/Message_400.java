package com.project.comgle.global.config.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class Message_400 {

    @Schema(description = SchemaDescriptionUtils.STATUS_CODE, example = "400")
    private int status;

    @Schema(description = SchemaDescriptionUtils.MESSAGE,example = "400 error message")
    private String message;
}
