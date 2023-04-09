package com.project.comgle.member.repository;

import com.project.comgle.company.entity.Company;
import com.project.comgle.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByMemberNameAndCompany(String memberName,Company company);

    Optional<Member> findByMemberNameStartingWithAndPhoneNum(String memberName, String phoneNum);

    Optional<Member> findByPhoneNum(String phoneNum);

    @Query("SELECT m from Member m where m.company = :company and m.valid = true")
    List<Member> findAllByCompany(@Param("company") Company company);

    Optional<Member> findByIdAndCompany(Long id, Company company);

}
