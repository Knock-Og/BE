package com.project.comgle.post.controller;

import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.config.swagger.Message_200;
import com.project.comgle.global.config.swagger.Message_403;
import com.project.comgle.global.config.swagger.Message_404;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.post.dto.SearchPageResponseDto;
import com.project.comgle.post.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "SEARCH", description = "게시글 조건 조회 관련 API Document")
public class SearchController {

    private final SearchService searchService;

    @ExeTimer
    @Operation(summary = "키워드 조회 API", description = "제목, 내용, 키워드로 검색하는 기능입니다.",
        parameters = {
            @Parameter(name = "page", description = "페이지 번호", in = ParameterIn.QUERY),
            @Parameter(name = "keyword", description = "검색 키워드", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬조건", in = ParameterIn.QUERY, example = "조회수 | 생성일자 | 댓글수 | 관심도"),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SearchPageResponseDto.class))),
        }
    )
    @GetMapping("/search")
    public SearchPageResponseDto keywordSearch(@RequestParam(value = "page", defaultValue = "1") int page,
                                                 @RequestParam(value = "keyword") String keyword,
                                                 @RequestParam(value = "sort", defaultValue = "조회수") String sortType,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return searchService.searchKeyword(page, keyword, sortType, userDetails.getCompany());
    }

    @ExeTimer
    @Operation(summary = "카테고리 조회 API", description = "해당 카테고리로 조회하는 기능입니다.",
        parameters = {
            @Parameter(name = "page", description = "페이지 번호", in = ParameterIn.QUERY),
            @Parameter(name = "category", description = "카테고리명", in = ParameterIn.QUERY),
            @Parameter(name = "sort", description = "정렬조건", in = ParameterIn.QUERY, example = "조회수 | 생성일자 | 댓글수 | 관심도"),
        }, responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SearchPageResponseDto.class))),
        }
    )
    @GetMapping("/category")
    public SearchPageResponseDto categorySearch(@RequestParam(value = "page", defaultValue = "1") int page,
                                                  @RequestParam(value = "category") String category,
                                                  @RequestParam(value = "sort", defaultValue = "조회수") String sortType,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return searchService.searchCategory(page, category, sortType, userDetails.getCompany());
    }

}
