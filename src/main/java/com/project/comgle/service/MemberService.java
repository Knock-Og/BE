package com.project.comgle.service;

import com.project.comgle.dto.request.CompanyRequestDto;
import com.project.comgle.dto.request.LoginRequestDto;
import com.project.comgle.dto.request.SignupRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.Company;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import com.project.comgle.jwt.JwtUtil;
import com.project.comgle.repository.CompanyRepository;
import com.project.comgle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletResponse;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ResponseEntity<MessageResponseDto> companyAdd(CompanyRequestDto companyRequestDto){
        String companyName = companyRequestDto.getCompanyName();
        String address = companyRequestDto.getAddress();
        String companyTel = companyRequestDto.getCompanyTel();
        String president = companyRequestDto.getPresident();
        String businessNum = companyRequestDto.getBusinessNum();
        String companyEmail = companyRequestDto.getCompanyEmail();

        Company company = Company.of(companyName,address,companyTel,president,businessNum,companyEmail);
        companyRepository.save(company);
        return ResponseEntity.ok()
                .body(MessageResponseDto.of(HttpStatus.OK.value(), "회사 추가 성공"));
    }

    public ResponseEntity<MessageResponseDto> signup(SignupRequestDto signupRequestDto){
        String memberName = signupRequestDto.getMemberName();
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        PositionEnum position = PositionEnum.valueOf(signupRequestDto.getPosition().trim().toUpperCase());
        boolean permission = false;
        String companyName = signupRequestDto.getCompanyName();

        Optional<Member> foundMember = memberRepository.findByEmail(email);
        if(foundMember.isPresent()){
            throw new IllegalArgumentException("이미 사용자가 존재합니다.");
        }

        Company foundCompany = companyRepository.findByCompanyName(companyName);
        if(foundCompany == null){
            throw new IllegalArgumentException("존재하지 않는 회사입니다.");
        }

        Member member = Member.of(memberName,email,password,position,permission,foundCompany);
        memberRepository.save(member);
        return ResponseEntity.ok()
                .body(MessageResponseDto.of(HttpStatus.OK.value(), "회원가입 성공"));
    }


    public ResponseEntity<MessageResponseDto> login(LoginRequestDto loginRequestDto, HttpServletResponse response){
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        Optional<Member> foundMember = memberRepository.findByEmail(email);
        if(foundMember.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 사용자 입니다.");
        }

        if(!passwordEncoder.matches(password, foundMember.get().getPassword())){
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER,jwtUtil.createToken(foundMember.get().getEmail()));

        return ResponseEntity.ok()
                .body(MessageResponseDto.of(HttpStatus.OK.value(), "로그인 성공"));
    }

}
