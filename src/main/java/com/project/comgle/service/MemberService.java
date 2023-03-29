package com.project.comgle.service;

import com.project.comgle.dto.request.CompanyRequestDto;
import com.project.comgle.dto.request.LoginRequestDto;
import com.project.comgle.dto.response.MemberResponseDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.Company;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import com.project.comgle.exception.CustomException;
import com.project.comgle.exception.ExceptionEnum;
import com.project.comgle.jwt.JwtUtil;
import com.project.comgle.repository.CompanyRepository;
import com.project.comgle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;



    @Transactional
    public ResponseEntity<MessageResponseDto> companyAdd(CompanyRequestDto companyRequestDto){
        Company findCompany = companyRepository.findByCompanyName(companyRequestDto.getCompanyName());
        if(findCompany != null){
            throw new CustomException(ExceptionEnum.DUPLICATE_COMPANY);
        }

        Company company = Company.from(companyRequestDto);
        companyRepository.save(company);

        return ResponseEntity.ok()
                .body(MessageResponseDto.of(HttpStatus.OK.value(), "회사 추가 성공"));
    }

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
}
