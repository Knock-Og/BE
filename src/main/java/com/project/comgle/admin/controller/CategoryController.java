package com.project.comgle.admin.controller;

import com.project.comgle.admin.dto.CategoryRequestDto;
import com.project.comgle.admin.dto.CategoryResponseDto;
import com.project.comgle.admin.service.CategoryService;
import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.global.config.swagger.Message_200;
import com.project.comgle.global.config.swagger.Message_400;
import com.project.comgle.global.config.swagger.Message_404;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.member.entity.PositionEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Secured(PositionEnum.Authority.ADMIN)
    @Operation(summary = "카테고리 생성 API", description = "ADMIN PAGE에서 사내 카테고리를 생성합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
            @ApiResponse(responseCode = "400", description = "400 에러", content = @Content(schema = @Schema(implementation = Message_400.class)))
        }
    )
    @PostMapping("/category")
    public SuccessResponse categoryAdd(@Valid @RequestBody CategoryRequestDto categoryRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return categoryService.addCategory(categoryRequestDto.getCategoryName().trim(), userDetails.getMember(), userDetails.getCompany());
    }

    @ExeTimer
    @Operation(summary = "모든 카테고리 조회 API", description = "사내 모든 카테고리를 조회합니다.",
            responses = @ApiResponse(responseCode = "200", description = "성공", content = @Content(array = @ArraySchema( schema = @Schema(implementation = CategoryResponseDto.class))))
    )
    @GetMapping("/categories")
    public List<CategoryResponseDto> categoryList(@AuthenticationPrincipal UserDetailsImpl userDetails){

        return categoryService.findCategories(userDetails.getCompany());
    }

    @Secured(PositionEnum.Authority.ADMIN)
    @Operation(summary = "카테고리 수정 API", description = "ADMIN PAGE에서 사내 해당 카테고리를 수정합니다.",
        parameters = @Parameter(name = "category-id", description = "카테고리 고유번호", in = ParameterIn.PATH),
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
                @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @PutMapping("/category/{category-id}")
    public SuccessResponse categoryModify( @PathVariable(name = "category-id") Long categoryId,
                                      @Valid @RequestBody CategoryRequestDto categoryRequestDto,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails){

        return categoryService.modifyCategory(categoryId, categoryRequestDto.getCategoryName().trim(), userDetails.getCompany());
    }

    @Secured(PositionEnum.Authority.ADMIN)
    @Operation(summary = "카테고리 삭제 API", description = "ADMIN PAGE에서 사내 해당 카테고리를 삭제합니다.",
        parameters = @Parameter(name = "category-id", description = "카테고리 고유번호", in = ParameterIn.PATH),
        responses = {
                @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Message_200.class))),
                @ApiResponse(responseCode = "404", description = "404 에러", content = @Content(schema = @Schema(implementation = Message_404.class)))
        }
    )
    @DeleteMapping("/category/{category-id}")
    public SuccessResponse delete(@PathVariable(name = "category-id") Long categoryId, @AuthenticationPrincipal UserDetailsImpl userDetails){

        return categoryService.deleteCategory(categoryId, userDetails.getCompany());
    }

}
