package com.project.comgle.repository;

import com.project.comgle.entity.Category;
import com.project.comgle.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository <Category,Long> {

    Optional<Category> findByCategoryName(String categoryName);

    Optional<Category> findByCategoryNameAndCompany(String categoryName, Company company);

    List<Category> findAllByCompany(Company company);

}
