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

    // 개발 단계용
    @PostConstruct
    @Transactional
    public void init(){
        List<Company> companyList = new ArrayList<>();
        List<Member> adminList = new ArrayList<>();
        companyList.add(Company.of("삼성","논현동","02-111-1111","이재용","123-45-67890","samsung@samsung.com"));
        companyList.add(Company.of("애플","벨리","02-111-1111","잡슨","123-45-67890","apple@apple.com"));
        companyRepository.saveAll(companyList);
        adminList.add(Member.of("삼성ADMIN","admin@samsung.com",passwordEncoder.encode("1234"),PositionEnum.ADMIN,companyList.get(0)));
        adminList.add(Member.of("애플ADMIN","admin@apple.com",passwordEncoder.encode("1234"),PositionEnum.ADMIN,companyList.get(1)));
        memberRepository.saveAll(adminList);
    }

    @Transactional
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
        checkEmail(signupRequestDto.getEmail(),company);

        Member newMember = Member.of(signupRequestDto,password,position,company);
        memberRepository.save(newMember);
        return ResponseEntity.ok(MessageResponseDto.of(HttpStatus.OK.value(), "회원가입 성공"));
    }


    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public void checkEmail(String email,Company company){

        Optional<Member> findMember = memberRepository.findByEmailAndCompany(email, company);

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
