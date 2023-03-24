package com.project.comgle.entity;


import com.project.comgle.security.UserDetailsImpl;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String memberName;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    private Log(String memberName, String content, Post post) {
        this.memberName = memberName;
        this.content = content;
        this.post = post;
    }

    public static Log of(Post post, String content, String memberName) {
        return Log.builder()
                .content(content)
                .memberName(memberName)
                .post(post)
                .build();
    }
}