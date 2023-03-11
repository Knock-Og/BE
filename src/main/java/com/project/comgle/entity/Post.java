package com.project.comgle.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(nullable = false, length = 20)
    private String modifyPermission;

    @Column(nullable = false, length = 20)
    private String readablePosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    private Post(String title, String content, String modifyPermission, String readablePosition, Member member, List<Comment> comments) {
        this.title = title;
        this.content = content;
        this.modifyPermission = modifyPermission;
        this.readablePosition = readablePosition;
        this.member = member;
        this.comments = comments;
    }
}