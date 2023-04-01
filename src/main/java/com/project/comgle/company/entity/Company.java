package com.project.comgle.company.entity;

import com.project.comgle.company.dto.CompanyRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String companyName;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(nullable = false, length = 20)
    private String companyTel;

    @Column(nullable = false, length = 20)
    private String president;

    @Column(nullable = false, length = 20)
    private String businessNum;

    @Column(nullable = false, length = 20)
    private String companyEmail;

    @Builder
    private Company(String companyName, String address, String companyTel, String president, String businessNum, String companyEmail) {
        this.companyName = companyName;
        this.address = address;
        this.companyTel = companyTel;
        this.president = president;
        this.businessNum = businessNum;
        this.companyEmail = companyEmail;
    }

    public static Company of(String companyName, String address, String companyTel, String president, String businessNum, String companyEmail) {
        return Company.builder()
                .companyName(companyName)
                .address(address)
                .companyTel(companyTel)
                .president(president)
                .businessNum(businessNum)
                .companyEmail(companyEmail)
                .build();
    }

    public static Company from(CompanyRequestDto companyRequestDto){
        return Company.builder()
                .companyName(companyRequestDto.getCompanyName())
                .address(companyRequestDto.getAddress())
                .companyTel(companyRequestDto.getCompanyTel())
                .president(companyRequestDto.getPresident())
                .businessNum(companyRequestDto.getBusinessNum())
                .companyEmail(companyRequestDto.getCompanyEmail())
                .build();
    }

}
