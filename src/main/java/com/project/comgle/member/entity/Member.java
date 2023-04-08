package com.project.comgle.member.entity;

import com.project.comgle.admin.dto.SignupRequestDto;
import com.project.comgle.company.entity.Company;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

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

    @Column(nullable = false, length = 20)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    private PositionEnum position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Builder
    private Member(String memberName, String email, String password,
                   PositionEnum position, Company company, String phoneNum) {
        this.memberName = memberName;
        this.email = email;
        this.password = password;
        this.position = position;
        this.company = company;
        this.phoneNum = phoneNum;
    }

    public static Member of(SignupRequestDto signupRequestDto, String password,
                            PositionEnum position, Company company) {
        return Member.builder()
                .memberName(signupRequestDto.getMemberName())
                .email(signupRequestDto.getEmail())
                .password(password)
                .position(position)
                .company(company)
                .phoneNum(signupRequestDto.getPhoneNum())
                .build();
    }

    public void updatePosition(PositionEnum position){
        this.position = position;
    }

    public void updatePwd(String password) {
        this.password = password;
    }

}
