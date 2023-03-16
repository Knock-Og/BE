package com.project.comgle.dto.response;

import com.project.comgle.entity.Post;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long id;
    private String memberName;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private String category;
//    List<Comment> comments;
    String[] keywords;

    @Builder
    public PostResponseDto(Long id, String memberName, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, String category, String[] keywords) {
        this.id = id;
        this.memberName = memberName;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.category = category;
//        this.comments= comments;
        this.keywords = keywords;
    }

    public static PostResponseDto of(Post post, String category,String[] keywords) {
        return PostResponseDto.builder()
                .id(post.getId())
                .memberName(post.getMember().getMemberName())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .category(category)
                .keywords(keywords)
                .build();
    }

}
