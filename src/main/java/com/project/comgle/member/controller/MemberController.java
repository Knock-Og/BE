package com.project.comgle.member.controller;

import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.common.response.ErrorResponse;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.global.config.swagger.Message_200;
import com.project.comgle.global.config.swagger.Message_400;
import com.project.comgle.global.config.swagger.Message_401;
import com.project.comgle.global.config.swagger.Message_404;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.member.dto.LoginRequestDto;
import com.project.comgle.member.dto.MemberResponseDto;
import com.project.comgle.member.dto.NewPasswordRequestDto;
import com.project.comgle.member.dto.PasswordRequestDto;
import com.project.comgle.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "MEMBER", description = "회원 관련 API Document")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "로그인 API", description = "사내 회원 로그인하는 기능입니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
            @ApiResponse(responseCode = "400", description = "400 에러", content = @Content(schema = @Schema(implementation = Message_400.class))),
            @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PostMapping("/login")
    public ResponseEntity<MessageResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return memberService.login(loginRequestDto);
    }

    @ExeTimer
    @Operation(summary = "전체 회원 조회 API", description = "해당 회사 모든 직원을 조회합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content( array = @ArraySchema( schema = @Schema(implementation = MemberResponseDto.class))))
        }
    )
    @GetMapping("/members")
    public List<MemberResponseDto> getMembers(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return memberService.findMembers(userDetails.getMember());
    }

    @Operation(summary = "비밀번호 확인 API", description = "비밀번호 변경 전 기전 비밀번호를 확인합니다.",
        responses = {
            @ApiResponse(responseCode = "400", description = "400 에러", content = @Content( schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PostMapping("/check/password")
    public SuccessResponse pwdCheck(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody PasswordRequestDto passwordRequestDto){
        return memberService.checkPwd(passwordRequestDto.getPassword(), userDetails.getMember());
    }

    @Operation(summary = "비밀번호 변경 API", description = "비밀번호를 변경합니다.",
        responses = {
            @ApiResponse(responseCode = "401", description = "401 에러", content = @Content( schema = @Schema(implementation = Message_401.class)))
        }
    )
    @PutMapping("/password")
    public SuccessResponse pwdUpdate(@RequestBody @Valid NewPasswordRequestDto passwordRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return memberService.updatePwd(passwordRequestDto.getNewPassword(), userDetails.getMember());
    }

}
