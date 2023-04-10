package com.project.comgle.admin.controller;

import com.project.comgle.admin.dto.SignupRequestDto;
import com.project.comgle.admin.service.AdminService;
import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.member.dto.PositionRequestDto;
import com.project.comgle.member.entity.PositionEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "ADMIN", description = "관리자 관련 API Document")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "회원가입 API", description = "회원을 등록합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured(PositionEnum.Authority.ADMIN)
    @PostMapping("/signup")
    public SuccessResponse memberAdd(@Valid @RequestBody SignupRequestDto signupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.addMember(signupRequestDto, userDetails.getMember(), userDetails.getCompany());
    }

    @Operation(summary = "회원탈퇴 API", description = "회원을 탈퇴시킵니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured(PositionEnum.Authority.ADMIN)
    @CacheEvict(value = "member", key = "#positionRequestDto.email")
    @DeleteMapping("/member/{member-id}")
    public SuccessResponse memberRemove(@PathVariable(name = "member-id") Long memberId, @RequestBody PositionRequestDto positionRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.removeMember(memberId, userDetails.getCompany());
    }

    @Operation(summary = "직책변경 API", description = "회원의 직책을 변경합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured(PositionEnum.Authority.ADMIN)
    @CacheEvict(value = "member", key = "#positionRequestDto.email")
    @PutMapping("/member/{member-id}/position")
    public SuccessResponse positionModify(@PathVariable(name = "member-id") Long memberId, @RequestBody PositionRequestDto positionRequestDto){
        return adminService.modifyPosition(memberId, positionRequestDto.getPosition());
    }

    @Operation(summary = "Email 중복 확인 API", description = "회원가입 시 Email 중복 확인합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/check/email/{email}")
    public SuccessResponse emailCheck(@PathVariable String email){
        adminService.checkEmail(email);
        return SuccessResponse.of(HttpStatus.OK,"사용 가능합니다.");
    }

    @Operation(summary = "회원명 중복 확인 API", description = "회원가입 시 회원명 중복 확인합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/check/name/{member-name}")
    public SuccessResponse nameCheck(@PathVariable(name = "member-name") String memberName, @AuthenticationPrincipal UserDetailsImpl userDetails){
        adminService.checkName(memberName,userDetails.getCompany());
        return SuccessResponse.of(HttpStatus.OK,"사용 가능합니다.");
    }

    @Operation(summary = "연락처 중복 확인 API", description = "회원가입 시 연락처 중복 확인합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/check/phone/{phone-num}")
    public SuccessResponse phoneCheck(@PathVariable(name = "phone-num") String phoneNum){
        adminService.checkPhone(phoneNum);
        return SuccessResponse.of(HttpStatus.OK,"사용 가능합니다.");
    }

}
