package com.project.comgle.post.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.comgle.post.entity.Post;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor
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
