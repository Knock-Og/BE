package com.project.comgle.bookmark.entity;

import com.project.comgle.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMarkFolder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bookMarkFolderName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public BookMarkFolder(String bookMarkFolderName, Member member) {
        this.bookMarkFolderName = bookMarkFolderName;
        this.member = member;
    }

    public static BookMarkFolder of(String bookMarkFolderName, Member member){
        return BookMarkFolder.builder()
                .bookMarkFolderName(bookMarkFolderName)
                .member(member)
                .build();
    }

    public void update(String bookMarkFolderName){
        this.bookMarkFolderName = bookMarkFolderName;
    }

}
