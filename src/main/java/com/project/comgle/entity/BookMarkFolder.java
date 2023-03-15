package com.project.comgle.entity;

import com.project.comgle.dto.request.BookMarkFolderRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    // 고민중
    /*@OneToMany(mappedBy = "bookMarkFolder", cascade = CascadeType.REMOVE)
    private List<BookMark> bookMarks = new ArrayList<>();*/

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
}
