package com.project.comgle.member.service;

import com.project.comgle.company.dto.CompanyRequestDto;
import com.project.comgle.company.entity.Company;
import com.project.comgle.company.repository.CompanyRepository;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.global.utils.JwtUtil;
import com.project.comgle.member.dto.LoginRequestDto;
import com.project.comgle.member.dto.MemberResponseDto;
import com.project.comgle.member.dto.PasswordRequestDto;
import com.project.comgle.member.entity.Member;
import com.project.comgle.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
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
    public ResponseEntity<MessageResponseDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response){

        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        Optional<Member> foundMember = memberRepository.findByEmail(email);
        if(foundMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        if(!passwordEncoder.matches(password, foundMember.get().getPassword())){
            throw new CustomException(ExceptionEnum.WORNG_PASSWORD);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER,jwtUtil.createToken(foundMember.get().getEmail()));

        return ResponseEntity.ok()
                .body(MessageResponseDto.of(HttpStatus.OK.value(), "로그인 성공"));
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
    public void checkPwd(String pwd, Member member){

        Optional<Member> findMember = memberRepository.findById(member.getId());

        if(!passwordEncoder.matches(pwd, findMember.get().getPassword())){
            throw new CustomException(ExceptionEnum.WORNG_PASSWORD);
        }
    }

    @Transactional
    public SuccessResponse updatePwd(PasswordRequestDto passwordRequestDto, Member member){

        Optional<Member> findMember = memberRepository.findById(member.getId());

        if(passwordEncoder.matches(passwordRequestDto.getPassword(), findMember.get().getPassword())){
            throw new CustomException(ExceptionEnum.DUPLICATE_PASSWORD);
        }

        String newPwdEndcode = passwordEncoder.encode(passwordRequestDto.getPassword());
        findMember.get().updatePwd(newPwdEndcode);

        return SuccessResponse.of(HttpStatus.OK, "Update Password Successful");
    }

}
