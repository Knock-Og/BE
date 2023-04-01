package com.project.comgle.auth.controller;

import com.project.comgle.auth.dto.FindEmailRequestDto;
import com.project.comgle.auth.dto.SmsResponseDto;
import com.project.comgle.auth.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "SMS", description = "SMS 전송 API Document")
public class SmsController {

    private final SmsService smsService;

    @Operation(summary = "SMS 코드 발송 API", description = "Email과 연락처를 통해서 유효한 회원인지 확인 후 SMS 코드를 발송 합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/sms")
    public ResponseEntity<SmsResponseDto> sendSmsCode(@RequestBody FindEmailRequestDto emailCheckRequestDto) {
        return smsService.sendSmsCode(emailCheckRequestDto);
    }

    @Operation(summary = "회원 Email 찾기 API", description = "SMS 코드 확인 후 EMAIL을 발송합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/member/email")
    public  ResponseEntity<Map<String,String>> findEmail(@RequestBody FindEmailRequestDto emailCheckRequestDto) {
        return smsService.findEmail(emailCheckRequestDto);
    }

}
