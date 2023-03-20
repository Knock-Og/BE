package com.project.comgle.dto.response;

import com.project.comgle.entity.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SearchResponseDto {
    private Long id;
    private String memberName;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String[] keywords;
    private int commentCount;

    @Builder
    private SearchResponseDto(Long id, String memberName, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, String[] keywords, int commentCount) {
        this.id = id;
        this.memberName = memberName;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.keywords = keywords;
        this.commentCount = commentCount;
    }


    public static SearchResponseDto of(Post post, String[] keywords, int commentCount) {
        return SearchResponseDto.builder()
                .id(post.getId())
                .memberName(post.getMember().getMemberName())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .keywords(keywords)
                .commentCount(commentCount)
                .build();
    }
}
