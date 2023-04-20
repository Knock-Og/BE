package com.project.comgle.global.config.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class Message_401 {
    
    @Schema(description = SchemaDescriptionUtils.STATUS_CODE, example = "401")
    private int status;

    @Schema(description = SchemaDescriptionUtils.MESSAGE,example = "401 error message")
    private String message;
}
