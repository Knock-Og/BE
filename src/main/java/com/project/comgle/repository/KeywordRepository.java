package com.project.comgle.repository;

import com.project.comgle.entity.Keyword;
import com.project.comgle.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    List<Keyword> findAllByPost(Post post);

    void deleteAllByPost(Post post);
}
