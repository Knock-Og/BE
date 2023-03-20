package com.project.comgle.controller;

import com.project.comgle.dto.response.PostResponseDto;
import com.project.comgle.dto.response.SearchResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/search")
    public List<SearchResponseDto> searchKeyword(@RequestParam("k") String keyword, UserDetailsImpl userDetails){
        return searchService.searchKeyword(keyword, userDetails.getMember());
    }

}
