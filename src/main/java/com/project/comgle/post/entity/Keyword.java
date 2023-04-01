package com.project.comgle.post.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",nullable = false)
    private Post post;

    @Builder
    private Keyword(String keyword, Post post) {
        this.keyword = keyword;
        this.post = post;
    }

    public static Keyword of(String keyword) {
        return Keyword.builder()
                .keyword(keyword)
                .build();
    }

    public void addPost(Post post){
        this.post = post;
        post.getKeywords().add(this);
    }
}
