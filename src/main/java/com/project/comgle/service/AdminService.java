package com.project.comgle.service;

import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.dto.response.PermissionResponseDto;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import com.project.comgle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    // 미승인 회원 전체 조회 기능
    @Transactional(readOnly = true)
    public List<PermissionResponseDto> findPermisionMembers() {
        // 개발 단계 -> 추후 permision false인 멤버들 반환
        List<Member> findMembers = memberRepository.findAllByPermission(false);
        List<PermissionResponseDto> permissionResponseDtos = new ArrayList<>();

        for (Member member : findMembers) {
            permissionResponseDtos.add(PermissionResponseDto.from(member));
        }

        return permissionResponseDtos;
    }

    // 회원가입 요청 회원 승인 기능
    @Transactional
    public MessageResponseDto permisionMember(Long memberId) {

        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalStateException("해당 멤버가 없습니다.")
        );

        if(findMember.isPermission()){
            throw new IllegalStateException("이미 승인된 유저입니다.");
        }

        findMember.permision();

        return MessageResponseDto.of(HttpStatus.OK.value(), "승인 완료");
    }

    // 회원 직책 수정 기능
    @Transactional
    public MessageResponseDto updatePosition(Long memberId, String pos) {

        PositionEnum position ;

        try{
            position = PositionEnum.valueOf(pos);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("해당 직책이 존재하지 않습니다.");
        }

        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalStateException("해당 멤버가 없습니다.")
        );

        if (PositionEnum.ADMIN == position){

            throw new IllegalArgumentException("ADMIN 계정으로 직책을 변경할 수 없습니다.");

        } else if(!findMember.isPermission()){

            throw new IllegalStateException("승인되지 않은 회원입니다.");

        } else if(findMember.getPosition() == PositionEnum.ADMIN){

            throw new IllegalStateException("ADMIN 계정은 직책 변경 불가합니다.");

        }

        findMember.updatePosition(position);

        return MessageResponseDto.of(HttpStatus.OK.value(), "변경 완료");
    }
}
