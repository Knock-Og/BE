package com.project.comgle.entity;

import com.project.comgle.dto.request.PostRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

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

    // 연관관계 추가
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Keyword> keywords = new ArrayList<>();

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

    public static Post from(PostRequestDto postRequestDto, Member member){
        return Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .modifyPermission(postRequestDto.getModifyPermission())
                .readablePosition(postRequestDto.getReadablePosition())
                .member(member)
                .build();
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

}