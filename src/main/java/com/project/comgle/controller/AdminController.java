package com.project.comgle.controller;

import com.project.comgle.dto.request.PositionRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping("/position/members/{member-id}")
    public MessageResponseDto updatePosition(@PathVariable(name = "member-id") Long memberId, @RequestBody PositionRequestDto positionRequestDto){
        return adminService.updatePosition(memberId, positionRequestDto.getPosition());
    }
}
