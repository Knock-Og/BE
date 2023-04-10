package com.project.comgle.post.repository;

import com.project.comgle.post.entity.Keyword;
import com.project.comgle.post.entity.Post;
import com.project.comgle.post.entity.QKeyword;
import com.project.comgle.post.entity.QPost;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class KeywordRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;
    private final QKeyword keyword = QKeyword.keyword1;
    private final QPost newPost = QPost.post;

    public List<Keyword> findAllByPost(Post post) {
        JPAQuery<Keyword> result = new JPAQuery<>(entityManager);
        return new ArrayList<>(result.select(keyword)
                .from(keyword)
                .innerJoin(keyword.post, newPost).fetchJoin()
                .where(newPost.id.eq(post.getId()))
                .distinct()
                .fetch());
    }

}
