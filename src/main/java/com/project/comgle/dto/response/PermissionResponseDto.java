package com.project.comgle.dto.response;

import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PermissionResponseDto {

    private Long id;
    private String memberName;
    private String email;
    private PositionEnum position;

    @Builder
    private PermissionResponseDto(Long id, String memberName, String email, PositionEnum position) {
        this.id = id;
        this.memberName = memberName;
        this.email = email;
        this.position = position;
    }

    public static PermissionResponseDto from(Member member){
        return PermissionResponseDto.builder()
                .id(member.getId())
                .memberName(member.getMemberName())
                .email(member.getEmail())
                .position(member.getPosition())
                .build();
    }
}
