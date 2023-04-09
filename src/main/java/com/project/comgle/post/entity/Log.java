package com.project.comgle.post.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String memberName;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String oldContent;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String newContent;

    @ElementCollection
    private List<Integer> changedLineNum;

    @Column(nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    private Log(String memberName, String content, String oldContent, String newContent, List<Integer> changedLineNum, Post post) {
        this.memberName = memberName;
        this.content = content;
        this.oldContent = oldContent;
        this.newContent = newContent;
        this.changedLineNum = changedLineNum;
        this.post = post;
    }

    public static Log of (String memberName, String oldContent, String newContent, List<Integer> changedLineNum, Post post) {
        return Log.builder()
                .memberName(memberName)
                .content(memberName + "님이 해당 페이지를 편집하였습니다.")
                .oldContent(oldContent)
                .newContent(newContent)
                .changedLineNum(changedLineNum)
                .post(post)
                .build();
    }
}