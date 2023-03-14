package com.project.comgle.controller;

import com.project.comgle.dto.request.PositionRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.dto.response.PermissionResponseDto;
import com.project.comgle.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    // 미승인 회원 전체 조회
    @GetMapping("/hold/members")
    public List<PermissionResponseDto> getPermissionMembers(){
        return adminService.findPermisionMembers();
    }

    @PostMapping("/permision/members/{member-id}")
    public MessageResponseDto permision(@PathVariable(name = "member-id") Long memberId){
        return adminService.permisionMember(memberId);
    }

    @PutMapping("/position/members/{member-id}")
    public MessageResponseDto updatePosition(@PathVariable(name = "member-id") Long memberId, @RequestBody PositionRequestDto positionRequestDto){
        return adminService.updatePosition(memberId, positionRequestDto.getPosition());
    }
}
