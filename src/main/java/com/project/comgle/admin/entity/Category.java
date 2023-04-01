package com.project.comgle.admin.entity;

import com.project.comgle.company.entity.Company;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID", nullable = false)
    private Company company;

    @Builder
    private Category(String categoryName, Company company) {
        this.categoryName = categoryName;
        this.company = company;
    }

    public static Category of(String categoryName, Company company){
        return Category.builder()
                .categoryName(categoryName)
                .company(company)
                .build();
    }

    public void update(String categoryName){
        this.categoryName = categoryName;
    }

}
