package com.project.comgle.post.repository;

import com.project.comgle.admin.entity.QCategory;
import com.project.comgle.member.entity.QMember;
import com.project.comgle.post.entity.Post;
import com.project.comgle.post.entity.QKeyword;
import com.project.comgle.post.entity.QPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class PostRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    private final QPost post = QPost.post;
    private final QMember member = QMember.member;
    private final QKeyword keyword = QKeyword.keyword1;
    private final QCategory category = QCategory.category;

    public Page<Post> findAllByContainingKeyword(int page, Set<String> keywords, String sortType, Long companyId) {
        BooleanBuilder builder = new BooleanBuilder();
        for (String key : keywords) {
            builder.or(post.content.like("%"+key+"%"))
                    .or(post.title.like("%"+key+"%"))
                    .or(keyword.keyword.like("%"+key+"%"));
        }

        builder.and(post.member.company.id.eq(companyId));
        JPAQuery<Post> result = new JPAQuery<>(entityManager);
        return new PageImpl<>(result.select(post)
                .from(post)
                .join(post.keywords, keyword).fetchJoin()
                .join(post.member, member).fetchJoin()
                .where(builder)
                .distinct()
                .offset((page-1)*10)
                .limit(10)
                .orderBy(sortingFilter(sortType))
                .fetch());
    }

    public List<Post> findAllByContainingKeywordCount(Set<String> keywords, Long companyId) {
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
                .fetch());
    }

    public Page<Post> findAllByContainingCategory(int page,  String c, String sortType, Long companyId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.member.company.id.eq(companyId));

        JPAQuery<Post> result = new JPAQuery<>(entityManager);
        return new PageImpl<>(result.select(post)
                .from(post)
                .join(post.category, category).fetchJoin()
                .where(builder
                        .and(post.category.categoryName.eq(c)))
                .distinct()
                .offset((page-1)*10)
                .limit(10)
                .orderBy(sortingFilter(sortType))
                .fetch());

    }

    public List<Post> findAllByContainingCategoryCount(String c, Long companyId) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(post.member.company.id.eq(companyId));
        JPAQuery<Post> result = new JPAQuery<>(entityManager);
        return new ArrayList<>(result.select(post)
                .from(post)
                .join(post.keywords, keyword).fetchJoin()
                .join(post.member, member).fetchJoin()
                .where(builder
                        .and(post.category.categoryName.eq(c)))
                .distinct()
                .fetch());
    }

   private OrderSpecifier<?> sortingFilter(String sortType) {
        Order order = Order.DESC;

        if (sortType.equals("관심도")) {
            return new OrderSpecifier<>(order, post.score);
        } else if (sortType.equals("조회수")) {
            return new OrderSpecifier<>(order, post.postViews);
        } else if (sortType.equals("댓글수")) {
            return new OrderSpecifier<>(order, post.comments.size());
        } else if (sortType.equals("생성일자")) {
            return new OrderSpecifier<>(order, post.createdAt);
        }

        return null;
   }
}

