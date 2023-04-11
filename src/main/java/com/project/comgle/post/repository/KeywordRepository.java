package com.project.comgle.post.repository;

import com.project.comgle.post.entity.Keyword;
import com.project.comgle.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    void deleteAllByPost(Post post);

}

