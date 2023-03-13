package com.project.comgle.service;

import com.project.comgle.dto.response.PermissionResponseDto;
import com.project.comgle.entity.Member;
import com.project.comgle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepository memberRepository;

    // 미승인 회원 전체 조회
    public List<PermissionResponseDto> findPermisionMembers() {
        // 개발 단계 -> 추후 permision false인 멤버들 반환
        List<Member> findMembers = memberRepository.findAllByPermission(false);
        List<PermissionResponseDto> permissionResponseDtos = new ArrayList<>();

        for (Member member : findMembers) {
            permissionResponseDtos.add(PermissionResponseDto.from(member));
        }

        return permissionResponseDtos;
    }
}
