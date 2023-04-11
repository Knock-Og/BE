package com.project.comgle.post.repository;

import com.project.comgle.post.entity.Log;
import com.project.comgle.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LogRepository extends JpaRepository<Log, Long> {

    List<Log> findAllByPostOrderByCreateDateDesc(Post post);

    void deleteAllByPost(Post post);

    void deleteAllByPostIn(List<Post> post);
}