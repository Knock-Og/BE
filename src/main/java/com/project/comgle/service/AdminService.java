package com.project.comgle.service;

import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import com.project.comgle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;

    // 회원 직책 수정 기능
    @Transactional
    public MessageResponseDto updatePosition(Long memberId, String pos) {

        PositionEnum position ;

        try{
            position = PositionEnum.valueOf(pos);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("해당 직책이 존재하지 않습니다.");
        }

        Optional<Member> findMember = memberRepository.findById(memberId);

        if( findMember.isEmpty() ){
            throw new IllegalArgumentException("해당 계정이 존재하지 않습니다.");
        } else if (PositionEnum.ADMIN == position){
            throw new IllegalArgumentException("ADMIN 계정으로 직책을 변경할 수 없습니다.");
        } else if(findMember.get().getPosition() == PositionEnum.ADMIN){
            throw new IllegalStateException("ADMIN 계정은 직책 변경 불가합니다.");
        }

        findMember.get().updatePosition(position);

        return MessageResponseDto.of(HttpStatus.OK.value(), "변경 완료");
    }
}
