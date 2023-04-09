package com.project.comgle.company.controller;

import com.project.comgle.company.dto.CompanyRequestDto;
import com.project.comgle.company.service.CompanyService;
import com.project.comgle.global.common.response.MessageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "COMPANY", description = "회사 관련 API Document")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "회사 등록 API", description = "서비스에 해당 회사를 등록합니다")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/company")
    public ResponseEntity<MessageResponseDto> companyAdd(@Valid @RequestBody CompanyRequestDto companyRequestDto){
        return companyService.addCompany(companyRequestDto);
    }
}
