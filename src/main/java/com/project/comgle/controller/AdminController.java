package com.project.comgle.controller;

import com.project.comgle.dto.request.PositionRequestDto;
import com.project.comgle.dto.request.SignupRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.signup(signupRequestDto, userDetails.getMember());
    }

    @Operation(summary = "직책변경 API", description = "회원의 직책을 변경합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/position/members/{member-id}")
    public MessageResponseDto updatePosition(@PathVariable(name = "member-id") Long memberId, @RequestBody PositionRequestDto positionRequestDto){
        return adminService.updatePosition(memberId, positionRequestDto.getPosition());
    }


    @Operation(summary = "Email 중복 확인 API", description = "회원가입 시 Email 중복 확인합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/check/email/{email}")
    public ResponseEntity<MessageResponseDto> checkEmail(@PathVariable String email){
        adminService.checkEmail(email);
        return ResponseEntity.ok(MessageResponseDto.of(HttpStatus.OK.value(),"사용 가능합니다."));
    }


    @Operation(summary = "회원명 중복 확인 API", description = "회원가입 시 회원명 중복 확인합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/check/name/{member-name}")
    public ResponseEntity<MessageResponseDto> checkName(@PathVariable(name = "member-name") String memberName, @AuthenticationPrincipal UserDetailsImpl userDetails){
        adminService.checkName(memberName,userDetails.getMember().getCompany());
        return ResponseEntity.ok(MessageResponseDto.of(HttpStatus.OK.value(),"사용 가능합니다."));
    }

}
