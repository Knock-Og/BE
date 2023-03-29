package com.project.comgle.controller;

import com.project.comgle.dto.common.SuccessResponse;
import com.project.comgle.dto.request.EmailAuthRequestDto;
import com.project.comgle.dto.response.EmailAuthResponseDto;
import com.project.comgle.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
@Tag(name = "MAILCONFIRM", description = "메일 인증 관련 API Document")
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "이메일 인증코드 전송 API", description = "이메일 인증 메일을 보냅니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/mail/auth")
    public SuccessResponse mailConfirm(@RequestBody EmailAuthRequestDto emailDto) throws MessagingException {

        return emailService.sendEmail(emailDto.getEmail());
    }

    @Operation(summary = "이메일 인증코드 확인 API", description = "이메일 인증 코드 확인 후 새로 생성된 비밀번호를 반환합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/member/pwd/{authenticationCode}")
    public EmailAuthResponseDto findPassword(@RequestBody EmailAuthRequestDto emailDto, @PathVariable(name = "authenticationCode") String code){
        return emailService.checkMailCode(emailDto.email, code);
    }
}
