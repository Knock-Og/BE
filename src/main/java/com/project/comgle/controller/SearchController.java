package com.project.comgle.controller;

import com.project.comgle.dto.response.PostResponseDto;
import com.project.comgle.dto.response.SearchResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "키워드 조회 API", description = "제목, 내용, 키워드로 검색하는 기능입니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/search")
    public List<SearchResponseDto> searchKeyword(@RequestParam("k") String keyword, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return searchService.searchKeyword(keyword, userDetails.getCompany());
    }

    @Operation(summary = "카테고리 조회 API", description = "해당 카테고리로 조회하는 기능입니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/category")
    public List<SearchResponseDto> searchCategory(@RequestParam("c") String category, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return searchService.searchCategory(category, userDetails.getMember());
    }

}
