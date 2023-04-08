package com.project.comgle.post.repository;

import com.project.comgle.member.entity.QMember;
import com.project.comgle.post.entity.Keyword;
import com.project.comgle.post.entity.Post;
import com.project.comgle.post.entity.QKeyword;
import com.project.comgle.post.entity.QPost;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Repository
@Transactional
public class PostRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    private final QPost post = QPost.post;
    private final QMember member = QMember.member;
    private final QKeyword keyword = QKeyword.keyword1;

    public List<Post> findAllByContainingKeyword(String k) {
        JPAQuery<Post> result = new JPAQuery<>(entityManager);
        return new ArrayList<>(result.select(post)
                .from(post)
                .innerJoin(post.member, member).fetchJoin()
                .innerJoin(post.keywords, keyword).fetchJoin()
                .where(post.title.contains(k)
                        .or(post.content.contains(k))
                        .or(keyword.keyword.contains(k)))
                .distinct()
                .fetch());
    }

}
