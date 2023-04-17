package com.project.comgle.post.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class SearchPageResponseDto {

    private int endPage;
    private int totalPost;

    private List<SearchResponseDto> searchResponseDtoList;

    @Builder
    private SearchPageResponseDto(int endPage, int totalPost, List<SearchResponseDto> searchResponseDtoList) {
        this.endPage = endPage;
        this.totalPost = totalPost;
        this.searchResponseDtoList = searchResponseDtoList;
    }

    public static SearchPageResponseDto of(int endPage, int totalPost, List<SearchResponseDto> searchResponseDtoList){
        return SearchPageResponseDto.builder()
                .endPage(endPage)
                .totalPost(totalPost)
                .searchResponseDtoList(searchResponseDtoList)
                .build();
    }

}
