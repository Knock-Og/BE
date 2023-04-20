package com.project.comgle.global.config.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class Message_201 {

    @Schema(description = SchemaDescriptionUtils.STATUS_CODE, example = "201")
    private int status;

    @Schema(description = SchemaDescriptionUtils.MESSAGE,example = "Success message")
    private String message;
}
