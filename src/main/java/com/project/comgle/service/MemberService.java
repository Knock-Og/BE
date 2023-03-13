package com.project.comgle.service;

import com.project.comgle.dto.request.CompanyRequestDto;
import com.project.comgle.dto.request.SignupRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.Company;
import com.project.comgle.entity.Member;
import com.project.comgle.jwt.JwtUtil;
import com.project.comgle.repository.CompanyRepository;
import com.project.comgle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        String position = signupRequestDto.getPosition();
        Boolean permission = true;
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
}
