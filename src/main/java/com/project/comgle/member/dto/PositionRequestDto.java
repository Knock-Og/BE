package com.project.comgle.member.dto;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PositionRequestDto {

    @Schema(description = SchemaDescriptionUtils.Member.POSITION,
            example = "MEMBER", allowableValues = {"MEM" +
            "BER", "MANAGER","OWNER"})
    private String position;

}
