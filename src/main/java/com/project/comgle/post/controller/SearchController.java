package com.project.comgle.post.controller;

import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.post.dto.SearchPageResponseDto;
import com.project.comgle.post.dto.SearchResponseDto;
import com.project.comgle.post.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "SEARCH", description = "게시글 조건 조회 관련 API Document")
public class SearchController {

    private final SearchService searchService;

    @ExeTimer
    @Operation(summary = "키워드 조회 API", description = "제목, 내용, 키워드로 검색하는 기능입니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/search")
    public SearchPageResponseDto keywordSearch(@RequestParam(value = "page") int page,
                                                 @RequestParam(value = "keyword") String keyword,
                                                 @RequestParam(value = "sort") String sortType,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails){
        return searchService.searchKeyword(page, keyword, sortType, userDetails.getCompany());
    }

    @ExeTimer
    @Operation(summary = "카테고리 조회 API", description = "해당 카테고리로 조회하는 기능입니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/category")
    public SearchPageResponseDto categorySearch(@RequestParam(value = "page") int page,
                                                  @RequestParam(value = "category") String category,
                                                  @RequestParam(value = "sort") String sortType,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails){
        return searchService.searchCategory(page, category, sortType, userDetails.getCompany());
    }

}
