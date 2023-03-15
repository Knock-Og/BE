package com.project.comgle.repository;

import com.project.comgle.entity.Company;
import com.project.comgle.entity.Post;
import com.project.comgle.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostCategoryRepository extends JpaRepository<PostCategory,Long> {

    Optional<PostCategory> findByPost(Post post);

}
