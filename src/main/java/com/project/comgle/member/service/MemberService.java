package com.project.comgle.member.service;

import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.global.utils.JwtUtil;
import com.project.comgle.member.dto.LoginRequestDto;
import com.project.comgle.member.dto.MemberResponseDto;
import com.project.comgle.member.entity.Member;
import com.project.comgle.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public ResponseEntity<MessageResponseDto> login(LoginRequestDto loginRequestDto){

        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        Optional<Member> foundMember = memberRepository.findByEmail(email);
        if(foundMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        } else if(!passwordEncoder.matches(password, foundMember.get().getPassword())){
            throw new CustomException(ExceptionEnum.WORNG_PASSWORD);
        }

        return ResponseEntity.ok()
                .header(JwtUtil.AUTHORIZATION_HEADER,jwtUtil.createToken(foundMember.get().getEmail()))
                .body(MessageResponseDto.of(HttpStatus.OK.value(), "You have successfully logged in."));
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findMembers(Member member) {

        List<Member> findMemberList = memberRepository.findAllByCompany(member.getCompany());

        List<MemberResponseDto> memberResponseDtos = new ArrayList<>();

        for (Member m : findMemberList) {
            memberResponseDtos.add(MemberResponseDto.from(m));
        }

        return memberResponseDtos;
    }

    @Transactional(readOnly = true)
    public SuccessResponse checkPwd(String pwd, Member member){

        Optional<Member> findMember = memberRepository.findById(member.getId());

        if(!passwordEncoder.matches(pwd, findMember.get().getPassword())){
            throw new CustomException(ExceptionEnum.WORNG_PASSWORD);
        }

        return SuccessResponse.of(HttpStatus.OK, "Correct password");
    }

    @Transactional
    public SuccessResponse updatePwd(String newPwd, Member member){

        Optional<Member> findMember = memberRepository.findById(member.getId());

        if(passwordEncoder.matches(newPwd, findMember.get().getPassword())){
            throw new CustomException(ExceptionEnum.DUPLICATE_PASSWORD);
        }

        String newPwdEndcode = passwordEncoder.encode(newPwd);
        findMember.get().updatePwd(newPwdEndcode);

        return SuccessResponse.of(HttpStatus.OK, "Your password has been successfully updated.");
    }

}
