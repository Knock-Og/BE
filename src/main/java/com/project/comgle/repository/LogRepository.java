package com.project.comgle.repository;

import com.project.comgle.entity.Log;
import com.project.comgle.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findAllByPostOrderByCreateDateDesc(Post post);
}