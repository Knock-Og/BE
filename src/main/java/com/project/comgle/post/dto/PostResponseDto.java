package com.project.comgle.post.dto;

import com.project.comgle.global.utils.SchemaDescriptionUtils;
import com.project.comgle.post.entity.Post;
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

    @Schema(description = SchemaDescriptionUtils.Post.POSTVIEWS)
    private int postViews;

    @Schema(description = SchemaDescriptionUtils.Post.EditingStatus)
    private String editingStatus;

    @Builder
    private PostResponseDto(Long id, String memberName, String title, String content, LocalDateTime createdAt, LocalDateTime modifiedAt, String category, String[] keywords, int postViews, String editingStatus) {
        this.id = id;
        this.memberName = memberName;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.category = category;
        this.keywords = keywords;
        this.postViews = postViews;
        this.editingStatus = editingStatus;
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

    public static PostResponseDto of(Post post, String category, String[] keywords, int postViews) {
        return PostResponseDto.builder()
                .id(post.getId())
                .memberName(post.getMember().getMemberName())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .category(category)
                .keywords(keywords)
                .postViews(postViews)
                .editingStatus(post.getEditingStatus())
                .build();
    }
}