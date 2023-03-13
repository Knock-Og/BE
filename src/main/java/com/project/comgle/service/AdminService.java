package com.project.comgle.service;

import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.dto.response.PermissionResponseDto;
import com.project.comgle.entity.Member;
import com.project.comgle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    // 미승인 회원 전체 조회
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
}
