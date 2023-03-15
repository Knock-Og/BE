package com.project.comgle.dto.response;

import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {

    private Long id;
    private String memberName;
    private String email;
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
