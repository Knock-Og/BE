package com.project.comgle.controller;

import com.project.comgle.dto.common.ErrorResponse;
import com.project.comgle.dto.request.CompanyRequestDto;
import com.project.comgle.dto.request.LoginRequestDto;
import com.project.comgle.dto.response.MemberResponseDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @Operation(summary = "회사 등록 API", description = "서비스에 해당 회사를 등록합니다")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/company")
    public ResponseEntity<MessageResponseDto> companyAdd(@Valid @RequestBody CompanyRequestDto companyRequestDto){
        return memberService.companyAdd(companyRequestDto);
    }

    @Operation(summary = "로그인 API", description = "로그인하는 기능입니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<MessageResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return memberService.login(loginRequestDto,response);
    }

    @Operation(summary = "회원 회원 조회 API", description = "사내 서비스에 등록된 모든 직원을 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/members")
    public List<MemberResponseDto> getMembers(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return memberService.findMembers(userDetails.getMember());
    }
}
