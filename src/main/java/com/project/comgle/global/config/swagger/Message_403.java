package com.project.comgle.global.config.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class Message_403 {
    
    @Schema(description = SchemaDescriptionUtils.STATUS_CODE, example = "403")
    private int status;

    @Schema(description = SchemaDescriptionUtils.MESSAGE,example = "403 error message")
    private String message;
}
