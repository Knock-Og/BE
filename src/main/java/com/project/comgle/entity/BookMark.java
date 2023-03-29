package com.project.comgle.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.module.FindException;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_folder_id")
    private BookMarkFolder bookMarkFolder;

    @Builder
    private BookMark(Member member, Post post, BookMarkFolder bookMarkFolder) {
        this.member = member;
        this.post = post;
        this.bookMarkFolder = bookMarkFolder;
    }

    public static BookMark of(Member member, Post post, BookMarkFolder bookMarkFolder){
        return BookMark.builder()
                .member(member)
                .post(post)
                .bookMarkFolder(bookMarkFolder)
                .build();
    }
}
