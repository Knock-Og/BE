package com.project.comgle.company.controller;

import com.project.comgle.company.dto.CompanyRequestDto;
import com.project.comgle.company.service.CompanyService;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.global.config.swagger.Message_200;
import com.project.comgle.global.config.swagger.Message_201;
import com.project.comgle.global.config.swagger.Message_400;
import com.project.comgle.global.config.swagger.Message_404;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "회사 등록 API", description = "서비스에 해당 회사를 등록합니다",
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
                @ApiResponse(responseCode = "400", description = "400 에러", content = @Content(schema = @Schema(implementation = Message_400.class)))
        }
    )
    @PostMapping("/company")
    public ResponseEntity<MessageResponseDto> companyAdd(@Valid @RequestBody CompanyRequestDto companyRequestDto){
        return companyService.addCompany(companyRequestDto);
    }
}
