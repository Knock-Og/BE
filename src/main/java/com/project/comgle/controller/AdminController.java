package com.project.comgle.controller;

import com.project.comgle.dto.response.PermissionResponseDto;
import com.project.comgle.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
