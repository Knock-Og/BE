package com.project.comgle.entity;

import com.project.comgle.dto.request.SignupRequestDto;
import com.project.comgle.entity.enumSet.PositionEnum;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Optional;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String memberName;

    @Column(nullable = false, length = 50)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private PositionEnum position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    private Member(String memberName, String email, String password, PositionEnum position, Company company) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.position = position;
        this.company = company;
    }

    public static Member of(SignupRequestDto signupRequestDto, String password, PositionEnum position, Company company) {
        return Member.builder()
                .memberName(signupRequestDto.getMemberName())
                .email(signupRequestDto.getEmail())
                .password(password)
                .position(position)
                .company(company)
                .build();
    }

    public static Member of(String memberName, String email, String password, PositionEnum position, Company company) {
        return Member.builder()
                .memberName(memberName)
                .email(email)
                .password(password)
                .position(position)
                .company(company)
                .build();
    }

    public void updatePosition(PositionEnum position){
        this.position = position;
    }
}