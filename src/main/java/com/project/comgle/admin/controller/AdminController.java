package com.project.comgle.admin.controller;

import com.project.comgle.admin.dto.EmailRequestDto;
import com.project.comgle.admin.dto.SignupRequestDto;
import com.project.comgle.admin.service.AdminService;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.global.config.swagger.Message_200;
import com.project.comgle.global.config.swagger.Message_400;
import com.project.comgle.global.config.swagger.Message_403;
import com.project.comgle.global.config.swagger.Message_404;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.member.dto.LoginRequestDto;
import com.project.comgle.member.dto.PositionRequestDto;
import com.project.comgle.member.entity.PositionEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "ADMIN", description = "관리자 관련 API Document")
public class AdminController {

    private final AdminService adminService;

    @Secured(PositionEnum.Authority.ADMIN)
    @Operation(summary = "회원가입 API", description = "회원을 등록합니다.",
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
                @ApiResponse(responseCode = "403", description = "403 에러", content = @Content(schema = @Schema(implementation = Message_403.class))),
                @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PostMapping("/signup")
    public SuccessResponse memberAdd(@Valid @RequestBody SignupRequestDto signupRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.addMember(signupRequestDto, userDetails.getMember(), userDetails.getCompany());
    }


    @Secured(PositionEnum.Authority.ADMIN)
    @Operation(summary = "회원탈퇴 API", description = "회원을 탈퇴시킵니다.",
        parameters = @Parameter(name = "member-id", description = "회원 고유번호", in = ParameterIn.PATH),
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
                @ApiResponse(responseCode = "403", description = "403 에러", content = @Content(schema = @Schema(implementation = Message_403.class))),
                @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @CacheEvict(value = "member", key = "#emailRequestDto.email")
    @DeleteMapping("/member/{member-id}")
    public SuccessResponse memberRemove(@PathVariable(name = "member-id") Long memberId, @RequestBody EmailRequestDto emailRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.removeMember(memberId, userDetails.getCompany());
    }

    @Secured(PositionEnum.Authority.ADMIN)
    @Operation(summary = "직책변경 API", description = "회원의 직책을 변경합니다.",
        parameters = @Parameter(name = "member-id", description = "회원 고유번호", in = ParameterIn.PATH),
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
                @ApiResponse(responseCode = "403", description = "403 에러", content = @Content(schema = @Schema(implementation = Message_403.class))),
                @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class))),
        }
    )
    @CacheEvict(value = "member", key = "#positionRequestDto.email")
    @PutMapping("/member/{member-id}/position")
    public SuccessResponse positionModify(@PathVariable(name = "member-id") Long memberId, @RequestBody PositionRequestDto positionRequestDto){
        return adminService.modifyPosition(memberId, positionRequestDto.getPosition());
    }

    @Operation(summary = "Email 중복 확인 API", description = "회원가입 시 Email 중복 확인합니다.",
        parameters = @Parameter(name = "email", description = "회원 이메일", in = ParameterIn.PATH),
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
                @ApiResponse(responseCode = "400", description = "400 에러", content = @Content(schema = @Schema(implementation = Message_400.class))),
        }
    )
    @GetMapping("/check/email/{email}")
    public SuccessResponse emailCheck(@PathVariable String email){
        adminService.checkEmail(email);
        return SuccessResponse.of(HttpStatus.OK,"This ID is available.");
    }

    @Operation(summary = "회원명 중복 확인 API", description = "회원가입 시 회원명 중복 확인합니다.",
        parameters = @Parameter(name = "member-name", description = "회원명", in = ParameterIn.PATH),
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
                @ApiResponse(responseCode = "400", description = "400 에러", content = @Content(schema = @Schema(implementation = Message_400.class)))
        }
    )
    @GetMapping("/check/name/{member-name}")
    public SuccessResponse nameCheck(@PathVariable(name = "member-name") String memberName, @AuthenticationPrincipal UserDetailsImpl userDetails){
        adminService.checkName(memberName,userDetails.getCompany());
        return SuccessResponse.of(HttpStatus.OK,"This member name is available.");
    }

    @Operation(summary = "연락처 중복 확인 API", description = "회원가입 시 연락처 중복 확인합니다.",
        parameters = @Parameter(name = "phone-num", description = "회원 연락처", in = ParameterIn.PATH),
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
                @ApiResponse(responseCode = "400", description = "400 에러", content = @Content(schema = @Schema(implementation = Message_400.class)))
        }
    )
    @GetMapping("/check/phone/{phone-num}")
    public SuccessResponse phoneCheck(@PathVariable(name = "phone-num") String phoneNum){
        adminService.checkPhone(phoneNum);
        return SuccessResponse.of(HttpStatus.OK,"This phone number is available.");
    }

    @Operation(summary = "ADMIN 로그인 API", description = "관리자 로그인하는 기능입니다.",
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
                @ApiResponse(responseCode = "400", description = "400 에러", content = @Content(schema = @Schema(implementation = Message_400.class))),
                @ApiResponse(responseCode = "403", description = "403 에러", content = @Content(schema = @Schema(implementation = Message_403.class))),
                @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PostMapping("/admin/login")
    public ResponseEntity<MessageResponseDto> login(@RequestBody LoginRequestDto loginRequestDto){
        return adminService.login(loginRequestDto);
    }
}
