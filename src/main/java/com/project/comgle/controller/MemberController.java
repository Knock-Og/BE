package com.project.comgle.controller;

import com.project.comgle.dto.request.CompanyRequestDto;

import com.project.comgle.dto.request.LoginRequestDto;

import com.project.comgle.dto.request.SignupRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/company")
    public ResponseEntity<MessageResponseDto> companyAdd(@Valid @RequestBody CompanyRequestDto companyRequestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("값을 제대로 입력 바랍니다.");
        }
        return memberService.companyAdd(companyRequestDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("이메일 형식이 아닙니다.");
        }
        return memberService.signup(signupRequestDto);
    }


    @PostMapping("/login")
    public ResponseEntity<MessageResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return memberService.login(loginRequestDto,response);
    }

    @GetMapping("/check")
    public ResponseEntity<MessageResponseDto> checkEmail(@RequestParam String email){
        return memberService.checkEmail(email);
    }
}
