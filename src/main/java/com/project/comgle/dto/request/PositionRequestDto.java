package com.project.comgle.dto.request;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PositionRequestDto {

    @Schema(description = SchemaDescriptionUtils.Member.POSITION,
            example = "MEMBER", allowableValues = {"MEMEBER", "MANAGER","OWNER"})
    private String position;
}
