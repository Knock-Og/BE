package com.project.comgle.controller;

import com.project.comgle.dto.request.CategoryRequestDto;
import com.project.comgle.dto.response.CategoryResponseDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.exception.ErrorResponse;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "CATEGORY", description = "카테고리 관련 API Document")
public class CategoryController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> validException(BindingResult result){
        log.error("error msg = {}",result.getFieldError().getDefaultMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), result.getFieldError().getDefaultMessage()));
    }

    private final CategoryService categoryService;

    @Operation(summary = "카테고리 생성 API", description = "ADMIN PAGE에서 사내 카테고리를 생성합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/category")
    public MessageResponseDto createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.create(categoryRequestDto.getCategoryName().trim(), userDetails.getMember(), userDetails.getCompany());
    }

    @Operation(summary = "모든 카테고리 조회 API", description = "사내 모든 카테고리를 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/categories")
    public List<CategoryResponseDto> findAll(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.findCategories(userDetails.getCompany());
    }

    @Operation(summary = "카테고리 수정 API", description = "ADMIN PAGE에서 사내 해당 카테고리를 수정합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/category/{category-id}")
    public MessageResponseDto update( @PathVariable(name = "category-id") Long categoryId,
                                      @Valid @RequestBody CategoryRequestDto categoryRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.updateCategory(categoryId, categoryRequestDto.getCategoryName() ,userDetails);
    }

    @Operation(summary = "카테고리 삭제 API", description = "ADMIN PAGE에서 사내 해당 카테고리를 삭제합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/categories/{category-id}")
    public MessageResponseDto delete(@PathVariable(name = "category-id") Long categoryId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.deleteCategory(categoryId, userDetails.getMember());
    }
}
