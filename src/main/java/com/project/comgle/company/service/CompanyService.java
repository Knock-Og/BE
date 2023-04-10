package com.project.comgle.company.service;

import com.project.comgle.company.dto.CompanyRequestDto;
import com.project.comgle.company.entity.Company;
import com.project.comgle.company.repository.CompanyRepository;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public ResponseEntity<MessageResponseDto> addCompany(CompanyRequestDto companyRequestDto){

        Optional<Company> findCompany = companyRepository.findByCompanyName(companyRequestDto.getCompanyName());

        if(findCompany.isPresent() && findCompany.get().isValid()){
            throw new CustomException(ExceptionEnum.DUPLICATE_COMPANY);
        } else if (findCompany.isEmpty()) {
            companyRepository.save(Company.from(companyRequestDto));
        } else{
            findCompany.get().reRegister();
        }

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "Your company has been added successfully."));
    }

}
