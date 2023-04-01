package com.project.comgle.post.repository;

import com.project.comgle.post.entity.Keyword;
import com.project.comgle.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    List<Keyword> findAllByPost(Post post);

    List<Keyword> findAllByKeywordContains(String keyword);
    void deleteAllByPost(Post post);
}
