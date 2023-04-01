package com.project.comgle.admin.service;

import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.member.dto.SignupRequestDto;
import com.project.comgle.company.entity.Company;
import com.project.comgle.member.entity.Member;
import com.project.comgle.member.entity.PositionEnum;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SuccessResponse updatePosition(Long memberId, String pos) {

        PositionEnum position ;

        try{
            position = PositionEnum.valueOf(pos.trim().toUpperCase());
        }catch (IllegalArgumentException e){
            throw new CustomException(ExceptionEnum.NOT_EXIST_POSITION);
        }

        Optional<Member> findMember = memberRepository.findById(memberId);

        if( findMember.isEmpty() ){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        } else if (PositionEnum.ADMIN == position){
            throw new CustomException(ExceptionEnum.IMMULATABLE_TO_ADMIN);
        } else if(findMember.get().getPosition() == PositionEnum.ADMIN){
            throw new CustomException(ExceptionEnum.IMMULATABLE_ADMIN_POSITION);
        }

        findMember.get().updatePosition(position);

        return SuccessResponse.of(HttpStatus.OK, "변경 완료");
    }

    @Transactional
    public SuccessResponse signup(SignupRequestDto signupRequestDto, Member member){

        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        PositionEnum position = PositionEnum.valueOf(signupRequestDto.getPosition().trim().toUpperCase());

        Company company = member.getCompany();

        if(company == null){
            throw new CustomException(ExceptionEnum.NOT_EXIST_COMPANY);
        } else if ( member.getPosition() != PositionEnum.ADMIN) {
            throw new CustomException(ExceptionEnum.REQUIRED_ADMIN_POSITION);
        }

        checkName(signupRequestDto.getMemberName(),company);
        checkEmail(signupRequestDto.getEmail());

        Member newMember = Member.of(signupRequestDto,password,position,company);
        memberRepository.save(newMember);
        return SuccessResponse.of(HttpStatus.OK, "회원가입 성공");
    }

    @Transactional(readOnly = true)
    public void checkEmail(String email){

        if(memberRepository.findByEmail(email).isPresent()){
            throw new CustomException(ExceptionEnum.DUPLICATE_EMAIL);
        }
    }

    @Transactional(readOnly = true)
    public void checkName(String memberName,Company company) {

        if(memberRepository.findByMemberNameAndCompany(memberName, company).isPresent()){
            throw new CustomException(ExceptionEnum.DUPLICATE_MEMBER);
        }
    }

}
