package com.project.comgle.member.controller;

import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.member.dto.LoginRequestDto;
import com.project.comgle.member.dto.MemberResponseDto;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.member.dto.NewPasswordRequestDto;
import com.project.comgle.member.dto.PasswordRequestDto;
import com.project.comgle.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "MEMBER", description = "회원 관련 API Document")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "로그인 API", description = "로그인하는 기능입니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<MessageResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return memberService.login(loginRequestDto);
    }

    @ExeTimer
    @Operation(summary = "전체 회원 조회 API", description = "해당 회사 모든 직원을 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/members")
    public List<MemberResponseDto> getMembers(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return memberService.findMembers(userDetails.getMember());
    }

    @Operation(summary = "비밀번호 확인 API", description = "비밀번호 변경 전 기전 비밀번호를 확인합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/check/password")
    public SuccessResponse pwdCheck(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody PasswordRequestDto passwordRequestDto){
        return memberService.checkPwd(passwordRequestDto.getPassword(), userDetails.getMember());
    }

    @Operation(summary = "비밀번호 변경 API", description = "비밀번호를 변경합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/password")
    public SuccessResponse pwdUpdate(@RequestBody @Valid NewPasswordRequestDto passwordRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return memberService.updatePwd(passwordRequestDto.getNewPassword(), userDetails.getMember());
    }

}
