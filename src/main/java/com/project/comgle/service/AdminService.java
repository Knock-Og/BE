package com.project.comgle.service;

import com.project.comgle.dto.request.SignupRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.Company;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import com.project.comgle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public ResponseEntity<MessageResponseDto> signup(SignupRequestDto signupRequestDto, Member member){

        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        PositionEnum position = PositionEnum.valueOf(signupRequestDto.getPosition().trim().toUpperCase());

        Company company = member.getCompany();

        if(company == null){
            throw new IllegalArgumentException("존재하지 않는 회사입니다.");
        } else if ( member.getPosition() != PositionEnum.ADMIN) {
            throw new IllegalArgumentException("ADMIN 권한이 필요합니다");
        }

        checkName(signupRequestDto.getMemberName(),company);
        checkEmail(signupRequestDto.getEmail());

        Member newMember = Member.of(signupRequestDto,password,position,company);
        memberRepository.save(newMember);
        return ResponseEntity.ok(MessageResponseDto.of(HttpStatus.OK.value(), "회원가입 성공"));
    }

    @Transactional(readOnly = true)
    public void checkEmail(String email){

        Optional<Member> findMember = memberRepository.findByEmail(email);

        if(findMember.isPresent()){
            throw new IllegalArgumentException("중복된 이메일이 존재합니다.");
        }

    }

    @Transactional(readOnly = true)
    public void checkName(String memberName,Company company) {

        Optional<Member> findMember = memberRepository.findByMemberNameAndCompany(memberName, company);

        if(findMember.isPresent()){
            throw new IllegalArgumentException("중복된 사용자명가 존재합니다.");
        }

    }
}
