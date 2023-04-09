package com.project.comgle.company.repository;

import com.project.comgle.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company,Long> {

    Optional<Company> findByCompanyName(String companyName);
}
