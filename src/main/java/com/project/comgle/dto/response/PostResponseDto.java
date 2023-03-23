package com.project.comgle.dto.response;

import com.project.comgle.dto.common.SchemaDescriptionUtils;
import com.project.comgle.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {

    @Schema(description = SchemaDescriptionUtils.ID )
    private Long id;

    @Schema(description = SchemaDescriptionUtils.Member.NAME , example = "user")
    private String memberName;

    @Schema(description = SchemaDescriptionUtils.Post.TITLE, example = "제목")
    private String title;

    @Schema(description = SchemaDescriptionUtils.Post.CONTENT, example = "내용")
    private String content;

    @Schema(description = SchemaDescriptionUtils.CREATE_AT)
    private LocalDateTime createdAt;

    @Schema(description = SchemaDescriptionUtils.MODIFIED_AT)
    private LocalDateTime modifiedAt;

    @Schema(description = SchemaDescriptionUtils.Category.NAME, example = "공지사항")
    private String category;


    @Schema(description = SchemaDescriptionUtils.Keyword.NAME)
    private String[] keywords;


    //    List<Comment> comments;

    @Builder
    private PostResponseDto(Long id, String memberName, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, String category, String[] keywords) {
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

    public static PostResponseDto of(Post post, String category, String[] keywords) {
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