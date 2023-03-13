package com.project.comgle.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CompanyRequestDto {
    private String companyName;
    private String address;
    private String companyTel;
    private String president;
    private String businessNum;
    private String companyEmail;
}
