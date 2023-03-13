package com.project.comgle.repository;

import com.project.comgle.entity.Company;
import com.project.comgle.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Company findByCompanyName(String companyName);
}
