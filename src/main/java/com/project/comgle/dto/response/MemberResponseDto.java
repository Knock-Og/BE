package com.project.comgle.dto.response;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    @Schema(description = SchemaDescriptionUtils.ID)
    private Long id;

    @Schema(description = SchemaDescriptionUtils.Member.NAME, example = "user")
    private String memberName;

    @Schema(description = SchemaDescriptionUtils.EMAIL , example = "test@xxx.xx")
    private String email;

    @Schema(description = SchemaDescriptionUtils.Member.POSITION,
            example = "MEMBER",allowableValues = {"MEMEBER", "MANAGER","OWNER"})
    private PositionEnum position;

    @Builder
    private MemberResponseDto(Long id, String memberName, String email, PositionEnum position) {
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.position = position;
    }

    public static MemberResponseDto from(Member member){
        return MemberResponseDto.builder()
                .id(member.getId())
                .memberName(member.getMemberName())
                .email(member.getEmail())
                .position(member.getPosition())
                .build();
    }
}
