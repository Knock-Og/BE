package com.project.comgle.auth.controller;

import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.auth.dto.EmailAuthRequestDto;
import com.project.comgle.auth.dto.EmailAuthResponseDto;
import com.project.comgle.auth.service.EmailService;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
@Tag(name = "MAILCONFIRM", description = "메일 인증 관련 API Document")
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "이메일 인증코드 전송 API", description = "이메일 인증 메일을 보냅니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/auth/email")
    public SuccessResponse emailCodeSend(@RequestBody EmailAuthRequestDto emailDto)  {

        try {
            return emailService.sendEmailCode(emailDto.getEmail());
        } catch (MessagingException e) {
            throw new CustomException(ExceptionEnum.SEND_EMAIL_CODE_ERR);
        }
    }

    @Operation(summary = "이메일 인증코드 확인 API", description = "이메일 인증 코드 확인 후 새로 생성된 비밀번호를 반환합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/member/pwd")
    public EmailAuthResponseDto passWordFind(@RequestBody EmailAuthRequestDto emailDto){

        return emailService.findPassWord(emailDto.getEmail(), emailDto.getAuthenticationCode());
    }

}
