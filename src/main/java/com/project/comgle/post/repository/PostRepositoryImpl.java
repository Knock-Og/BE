package com.project.comgle.post.repository;

import com.project.comgle.member.entity.QMember;
import com.project.comgle.post.entity.Post;
import com.project.comgle.post.entity.QKeyword;
import com.project.comgle.post.entity.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class PostRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    private final QPost post = QPost.post;
    private final QMember member = QMember.member;
    private final QKeyword keyword = QKeyword.keyword1;

    public List<Post> findAllByContainingKeyword(List<String> keywords,Long companyId) {
        BooleanBuilder builder = new BooleanBuilder();
        for (String key : keywords) {
            builder.or(post.content.like("%"+key+"%"))
                    .or(post.title.like("%"+key+"%"))
                    .or(keyword.keyword.like("%"+key+"%"));
        }

        builder.and(post.member.company.id.eq(companyId));
        JPAQuery<Post> result = new JPAQuery<>(entityManager);
        return new ArrayList<>(result.select(post)
                .from(post)
                .join(post.keywords, keyword).fetchJoin()
                .join(post.member, member).fetchJoin()
                .where(builder)
                .distinct()
                .orderBy(post.score.desc())
                .fetch());
    }

}

