package com.project.comgle.repository;

import com.project.comgle.entity.Company;
import com.project.comgle.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCategoryRepository extends JpaRepository<PostCategory,Long> {

}
