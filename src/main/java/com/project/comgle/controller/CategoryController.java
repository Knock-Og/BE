package com.project.comgle.controller;

import com.project.comgle.dto.request.CategoryRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    public MessageResponseDto create(@RequestBody CategoryRequestDto categoryRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.create(categoryRequestDto.getCategoryName().trim(), userDetails.getUser());
    }

    @DeleteMapping("/categories/{category-id}")
    public MessageResponseDto delete(@PathVariable(name = "category-id") Long categoryId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        log.info("role = {}" , userDetails.getAuthorities());
        return categoryService.deleteCategory(categoryId, userDetails.getUser());
    }
}
