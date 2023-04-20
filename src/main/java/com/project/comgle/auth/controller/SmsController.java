package com.project.comgle.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.comgle.auth.dto.EmailResponseDto;
import com.project.comgle.auth.dto.SendSmsRequestDto;
import com.project.comgle.auth.dto.FindEmailRequestDto;
import com.project.comgle.auth.dto.FindEmailResponseDto;
import com.project.comgle.auth.service.SmsService;
import com.project.comgle.global.config.swagger.Message_400;
import com.project.comgle.global.config.swagger.Message_404;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@Tag(name = "AUTHENTICATION", description = "회원 계정 찾기 관련 인증 API Document")
public class SmsController {

    private final SmsService smsService;

    @Operation(summary = "SMS 코드 발송 API", description = "Email + 연락처를 통해서 유효한 회원인지 확인 후 SMS 코드를 발송 합니다.",
        responses = {
            @ApiResponse(responseCode = "202", description = "성공", content = @Content(schema = @Schema(implementation = FindEmailResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "400 에러", content = @Content(schema = @Schema(implementation = Message_400.class))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PostMapping("/auth/sms")
    public ResponseEntity<FindEmailResponseDto> smsCodeSend(@RequestBody SendSmsRequestDto emailCheckRequestDto) {

        try {
            return smsService.sendSmsCode(emailCheckRequestDto);
        } catch (UnsupportedEncodingException | URISyntaxException | NoSuchAlgorithmException | InvalidKeyException |
                 JsonProcessingException e) {
            throw new CustomException(ExceptionEnum.SEND_SMS_CODE_ERR);
        }
    }

    @Operation(summary = "회원 Email 찾기 API", description = "SMS 코드 확인 후 EMAIL을 발송합니다.",
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = EmailResponseDto.class))),
                @ApiResponse(responseCode = "400", description = "400 에러", content = @Content(schema = @Schema(implementation = Message_400.class))),
                @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PostMapping("/member/email")
    public  ResponseEntity<EmailResponseDto> emailFind(@RequestBody FindEmailRequestDto emailCheckRequestDto) {
        return smsService.findEmail(emailCheckRequestDto);
    }

}
