package com.project.comgle.comment.entity;

import com.project.comgle.post.entity.Post;
import com.project.comgle.comment.dto.CommentRequestDto;
import com.project.comgle.member.entity.Member;
import com.project.comgle.global.entity.Timestamped;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Comment(String comment,Post post, Member member) {
        this.comment = comment;
        this.post = post;
        this.member = member;
    }

    public static Comment of(CommentRequestDto commentRequestDto, Member member, Post post) {
        return Comment.builder()
                .comment(commentRequestDto.getComment())
                .member(member)
                .post(post)
                .build();
    }

    public void updateComment(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }

}