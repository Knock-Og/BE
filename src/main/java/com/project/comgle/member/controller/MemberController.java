package com.project.comgle.member.controller;

import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.member.dto.LoginRequestDto;
import com.project.comgle.member.dto.MemberResponseDto;
import com.project.comgle.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
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
    public ResponseEntity<MessageResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return memberService.login(loginRequestDto,response);
    }

    @ExeTimer
    @Operation(summary = "전체 회원 조회 API", description = "해당 회사 모든 직원을 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/members")
    public List<MemberResponseDto> getMembers(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return memberService.findMembers(userDetails.getMember());
    }

}
