package com.project.comgle.member.repository;

import com.project.comgle.company.entity.Company;
import com.project.comgle.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByMemberNameAndCompany(String memberName,Company company);
    Optional<Member> findByMemberNameStartingWithAndPhoneNum(String memberName, String phoneNum);
    Optional<Member> findByPhoneNum(String phoneNum);
    List<Member> findAllByCompany(Company company);

}
