package com.project.comgle.post.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class SearchPageResponseDto {

    private int endPage;

    private List<SearchResponseDto> searchResponseDtoList;

    @Builder
    private SearchPageResponseDto(int endPage, List<SearchResponseDto> searchResponseDtoList) {
        this.endPage = endPage;
        this.searchResponseDtoList = searchResponseDtoList;
    }

    public static SearchPageResponseDto of(int endPage, List<SearchResponseDto> searchResponseDtoList){
        return SearchPageResponseDto.builder()
                .endPage(endPage)
                .searchResponseDtoList(searchResponseDtoList)
                .build();
    }

}
