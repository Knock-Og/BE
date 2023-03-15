package com.project.comgle.controller;

import com.project.comgle.dto.request.PositionRequestDto;
import com.project.comgle.dto.request.SignupRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.signup(signupRequestDto, userDetails.getUser());
    }

    @PutMapping("/position/members/{member-id}")
    public MessageResponseDto updatePosition(@PathVariable(name = "member-id") Long memberId, @RequestBody PositionRequestDto positionRequestDto){
        return adminService.updatePosition(memberId, positionRequestDto.getPosition());
    }

    @GetMapping("/check/email/{email}")
    public ResponseEntity<MessageResponseDto> checkEmail(@PathVariable String email, @AuthenticationPrincipal UserDetailsImpl userDetails ){
        adminService.checkEmail(email,userDetails.getUser().getCompany());
        return ResponseEntity.ok(MessageResponseDto.of(HttpStatus.OK.value(),"사용 가능합니다."));
    }

    @GetMapping("/check/name/{member-name}")
    public ResponseEntity<MessageResponseDto> checkName(@PathVariable(name = "member-name") String memberName, @AuthenticationPrincipal UserDetailsImpl userDetails){
        adminService.checkName(memberName,userDetails.getUser().getCompany());
        return ResponseEntity.ok(MessageResponseDto.of(HttpStatus.OK.value(),"사용 가능합니다."));
    }

}
