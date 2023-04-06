package com.project.comgle.admin.controller;

import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.admin.dto.CategoryRequestDto;
import com.project.comgle.admin.dto.CategoryResponseDto;
import com.project.comgle.member.entity.PositionEnum;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.admin.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CATEGORY", description = "카테고리 관련 API Document")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 생성 API", description = "ADMIN PAGE에서 사내 카테고리를 생성합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured(PositionEnum.Authority.ADMIN)
    @PostMapping("/category")
    public SuccessResponse createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.create(categoryRequestDto.getCategoryName().trim(), userDetails.getMember(), userDetails.getCompany());
    }

    @ExeTimer
    @Operation(summary = "모든 카테고리 조회 API", description = "사내 모든 카테고리를 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/categories")
    public List<CategoryResponseDto> findAll(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.findCategories(userDetails.getCompany());
    }

    @Operation(summary = "카테고리 수정 API", description = "ADMIN PAGE에서 사내 해당 카테고리를 수정합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured(PositionEnum.Authority.ADMIN)
    @PutMapping("/category/{category-id}")
    public SuccessResponse update( @PathVariable(name = "category-id") Long categoryId,
                                      @Valid @RequestBody CategoryRequestDto categoryRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.updateCategory(categoryId, categoryRequestDto.getCategoryName(), userDetails.getMember(), userDetails.getCompany());
    }

    @Operation(summary = "카테고리 삭제 API", description = "ADMIN PAGE에서 사내 해당 카테고리를 삭제합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @Secured(PositionEnum.Authority.ADMIN)
    @DeleteMapping("/category/{category-id}")
    public SuccessResponse delete(@PathVariable(name = "category-id") Long categoryId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.deleteCategory(categoryId, userDetails.getMember(), userDetails.getCompany());
    }

}
