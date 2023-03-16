package com.project.comgle.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID", nullable = false)
    private Company company;

    //Post 담당자와 추후 협의할 사항
    @Builder
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    @Builder
    public Category(String categoryName, Company company) {
        this.categoryName = categoryName;
        this.company = company;
    }

    public void update(String categoryName){
        this.categoryName = categoryName;
    }


}
