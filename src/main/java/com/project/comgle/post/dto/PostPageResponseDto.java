package com.project.comgle.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PostPageResponseDto {

    private int endPage;

    private List<PostResponseDto> postResponseDtoList;

    @Builder
    private PostPageResponseDto(int endPage, List<PostResponseDto> postResponseDtoList) {
        this.endPage = endPage;
        this.postResponseDtoList = postResponseDtoList;
    }

    public static PostPageResponseDto of(int endPage, List<PostResponseDto> postResponseDtoList){
        return PostPageResponseDto.builder()
                .endPage(endPage)
                .postResponseDtoList(postResponseDtoList)
                .build();
    }

}
