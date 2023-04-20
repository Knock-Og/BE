package com.project.comgle.auth.controller;

import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.auth.dto.EmailAuthRequestDto;
import com.project.comgle.auth.dto.EmailAuthResponseDto;
import com.project.comgle.auth.service.EmailService;
import com.project.comgle.global.config.swagger.*;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;

@RestController
@RequiredArgsConstructor
@Tag(name = "AUTHENTICATION", description = "회원 계정 찾기 관련 인증 API Document")
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "이메일 인증코드 전송 API", description = "이메일 인증 메일을 보냅니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Message_201.class))),
            @ApiResponse(responseCode = "400", description = "400 에러", content = @Content(schema = @Schema(implementation = Message_400.class))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PostMapping("/auth/email")
    public SuccessResponse emailCodeSend(@RequestBody EmailAuthRequestDto emailDto)  {

        try {
            return emailService.sendEmailCode(emailDto.getEmail());
        } catch (MessagingException e) {
            throw new CustomException(ExceptionEnum.SEND_EMAIL_CODE_ERR);
        }
    }

    @Operation(summary = "이메일 인증코드 확인 API", description = "이메일 인증 코드 확인 후 새로 생성된 비밀번호를 반환합니다.",
        responses = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = EmailAuthResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "400 에러", content = @Content(schema = @Schema(implementation = Message_400.class))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PostMapping("/member/pwd")
    public EmailAuthResponseDto passwordFind(@RequestBody EmailAuthRequestDto emailDto){

        return emailService.findPassword(emailDto.getEmail(), emailDto.getAuthenticationCode());
    }

}
