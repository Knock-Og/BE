package com.project.comgle.global.config.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class Message_404 {
    
    @Schema(description = SchemaDescriptionUtils.STATUS_CODE, example = "404")
    private int status;

    @Schema(description = SchemaDescriptionUtils.MESSAGE,example = "404 error message")
    private String message;
}
