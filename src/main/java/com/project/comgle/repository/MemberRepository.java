package com.project.comgle.repository;

import com.project.comgle.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberName(String memberName);
    List<Member> findAllByPermission(boolean permision);
}
