package com.project.comgle.admin.service;

import com.project.comgle.admin.dto.SignupRequestDto;
import com.project.comgle.bookmark.repository.BookMarkFolderRepository;
import com.project.comgle.bookmark.repository.BookMarkRepository;
import com.project.comgle.company.dto.CompanyRequestDto;
import com.project.comgle.company.entity.Company;
import com.project.comgle.company.repository.CompanyRepository;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.member.entity.Member;
import com.project.comgle.member.entity.PositionEnum;
import com.project.comgle.member.repository.MemberRepository;
import com.project.comgle.post.repository.LogRepository;
import com.project.comgle.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {


    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;
    private final BookMarkRepository bookMarkRepository;
    private final BookMarkFolderRepository bookMarkFolderRepository;
    private final LogRepository logRepository;

    @Transactional
    public SuccessResponse modifyPosition(Long memberId, String pos) {

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

        return SuccessResponse.of(HttpStatus.OK, "Change successful"); // 변경 완료
    }

    @Transactional
    public SuccessResponse addMember(SignupRequestDto signupRequestDto, Member member, Company company){

        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        PositionEnum position = PositionEnum.valueOf(signupRequestDto.getPosition().trim().toUpperCase());

        if(Objects.isNull(company)){
            throw new CustomException(ExceptionEnum.NOT_EXIST_COMPANY);
        } else if ( member.getPosition() != PositionEnum.ADMIN) {
            throw new CustomException(ExceptionEnum.REQUIRED_ADMIN_POSITION);
        }

        checkName(signupRequestDto.getMemberName(),company);
        checkEmail(signupRequestDto.getEmail());
        checkPhone(signupRequestDto.getPhoneNum());

        memberRepository.save(Member.of(signupRequestDto,password,position,company));

        return SuccessResponse.of(HttpStatus.OK, "join membership successful"); //회원 추가 성공
    }

    @Transactional
    public SuccessResponse removeMember(Long memberId, Company company) {

        Optional<Member> findMember = memberRepository.findByIdAndCompany(memberId, company);

        if(findMember.isEmpty() || !findMember.get().isValid()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        } else if(findMember.get().getPosition() == PositionEnum.ADMIN){
            throw new CustomException(ExceptionEnum.NOT_DELETE_ADMIN_POSITION);
        }

        findMember.get().withdrawal();

        return SuccessResponse.of(HttpStatus.OK, "Member deletion successful"); //회원  삭제 성공
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

    @Transactional(readOnly = true)
    public void checkPhone(String phoneNum) {

        if(memberRepository.findByPhoneNum(phoneNum).isPresent()){
            throw new CustomException(ExceptionEnum.DUPLICATE_PHONE_NUMBER);
        }
    }

}
