package com.project.comgle.post.entity;

import com.project.comgle.admin.entity.Category;
import com.project.comgle.comment.entity.Comment;
import com.project.comgle.global.entity.Timestamped;
import com.project.comgle.member.entity.Member;
import com.project.comgle.post.dto.PostRequestDto;
import com.project.comgle.member.entity.PositionEnum;
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

    @Column(nullable = false)
    private int postViews = 0;

    @Column(nullable = false)
    private int score = 0;

    // 편집 중 = true
    @Column(nullable = false)
    private String editingStatus = "false";

    @Enumerated(EnumType.STRING)
    private PositionEnum modifyPermission;

    @Enumerated(EnumType.STRING)
    private PositionEnum readablePosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // 연관관계 추가
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Keyword> keywords = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    private Post(String title, String content, PositionEnum modifyPermission, PositionEnum readablePosition, Member member,Category category, List<Comment> comments, int postViews, int score, String editingStatus) {
        this.title = title;
        this.content = content;
        this.modifyPermission = modifyPermission;
        this.readablePosition = readablePosition;
        this.category = category;
        this.member = member;
        this.comments = comments;
        this.postViews = postViews;
        this.score = score;
        this.editingStatus = editingStatus;
    }

    public static Post from(PostRequestDto postRequestDto, Category category,Member member){
        return Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .editingStatus(postRequestDto.getEditingStatus())
                .modifyPermission(PositionEnum.valueOf(postRequestDto.getModifyPermission().strip().toUpperCase()))
                .readablePosition(PositionEnum.valueOf(postRequestDto.getReadablePosition().strip().toUpperCase()))
                .category(category)
                .member(member)
                .build();
    }

    public void update(PostRequestDto postRequestDto,Category category) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.editingStatus = "false";
        this.category = category;
    }

    public void updateMethod(int weight) {
        this.postViews += (weight==3) ? 1 : 0;
        this.score += weight;
    }

    public void updateStatus(String editingStatus) {
        this.editingStatus = editingStatus;
    }

}