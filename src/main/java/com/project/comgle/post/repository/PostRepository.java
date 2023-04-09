package com.project.comgle.post.repository;

import com.project.comgle.member.entity.Member;
import com.project.comgle.member.entity.QMember;
import com.project.comgle.post.entity.Post;
import com.project.comgle.post.entity.QPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static com.project.comgle.admin.entity.QCategory.category;
import static com.querydsl.jpa.JPAExpressions.select;
import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static com.querydsl.jpa.JPAExpressions.*;
import static com.querydsl.core.types.dsl.Expressions.*;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public interface PostRepository extends JpaRepository<Post,Long> {

    @Query("SELECT p FROM Post p WHERE p.valid = true and p.member = :member")
    Page<Post> findAllByMember(@Param("member") Member member, Pageable pageable);
    Optional<Post> findByIdAndMember(Long postId, Member member);
    int countByCategoryId(Long categoryId);
    int countByMember(Member member);
    Page<Post> findAllByCategoryId(Long categoryId, Pageable pageRequest);

}


