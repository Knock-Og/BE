package com.project.comgle.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostPageResponseDto {

    private int endPage;
    private int totalPost;

    private List<PostResponseDto> postResponseDtoList;

    @Builder
    private PostPageResponseDto(int endPage, int totalPost, List<PostResponseDto> postResponseDtoList) {
        this.endPage = endPage;
        this.totalPost = totalPost;
        this.postResponseDtoList = postResponseDtoList;
    }

    public static PostPageResponseDto of(int endPage, int totalPost, List<PostResponseDto> postResponseDtoList){
        return PostPageResponseDto.builder()
                .endPage(endPage)
                .totalPost(totalPost)
                .postResponseDtoList(postResponseDtoList)
                .build();
    }

}
