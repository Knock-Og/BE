package com.project.comgle.member.dto;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PositionRequestDto {

    @Schema(description = SchemaDescriptionUtils.Member.POSITION,
            example = "MEMBER", allowableValues = {"MEMBER", "MANAGER","OWNER"})
    private String position;

    private String email;

}
