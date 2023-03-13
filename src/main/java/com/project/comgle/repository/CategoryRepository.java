package com.project.comgle.repository;

import com.project.comgle.entity.Category;
import com.project.comgle.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository <Category,Long> {

}
