package com.project.comgle.company.repository;

import com.project.comgle.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Company findByCompanyName(String companyName);

}
