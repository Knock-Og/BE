package com.project.comgle.controller;

import com.project.comgle.dto.request.CategoryRequestDto;
import com.project.comgle.dto.response.CategoryResponseDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.exception.ErrorResponse;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.CategoryService;
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
public class CategoryController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validException(BindingResult result){
        log.error("error msg = {}",result.getFieldError().getDefaultMessage());
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.value(), result.getFieldError().getDefaultMessage()));
    }

    private final CategoryService categoryService;

    @PostMapping("/categories")
    public MessageResponseDto createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.create(categoryRequestDto.getCategoryName().trim(), userDetails.getMember(), userDetails.getCompany());
    }

    @GetMapping("/categories")
    public List<CategoryResponseDto> findAll(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.findCategories(userDetails.getCompany());
    }

    @PutMapping("/categories/{category-id}")
    public MessageResponseDto update( @PathVariable(name = "category-id") Long categoryId,
                                      @Valid @RequestBody CategoryRequestDto categoryRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.updateCategory(categoryId, categoryRequestDto.getCategoryName() ,userDetails);
    }

    @DeleteMapping("/categories/{category-id}")
    public MessageResponseDto delete(@PathVariable(name = "category-id") Long categoryId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return categoryService.deleteCategory(categoryId, userDetails.getMember());
    }
}
