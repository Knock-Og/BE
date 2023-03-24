package com.project.comgle.service;

import com.project.comgle.dto.common.SuccessResponse;
import com.project.comgle.dto.response.CategoryResponseDto;
import com.project.comgle.entity.Category;
import com.project.comgle.entity.Company;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import com.project.comgle.exception.CustomException;
import com.project.comgle.exception.ExceptionEnum;
import com.project.comgle.repository.CategoryRepository;
import com.project.comgle.repository.CompanyRepository;
import com.project.comgle.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public SuccessResponse create(String categoryName, Member member, Company company) {

        isAdmin(member);

        Optional<Category> findCategory = categoryRepository.findByCategoryNameAndCompany(categoryName, company);

        if(findCategory.isPresent()){
            throw new CustomException(ExceptionEnum.DUPLICATE_CATEGORY);
        }

        categoryRepository.save(Category.of(categoryName, member.getCompany()));

        return SuccessResponse.of(HttpStatus.OK, "카테고리 추가 완료");

    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findCategories(Company company) {

        List<Category> categoryList = categoryRepository.findAllByCompany(company);

        return categoryList.stream().map(CategoryResponseDto::from).collect(Collectors.toList());
    }

    @Transactional
    public SuccessResponse updateCategory(Long categoryId, String categoryName, Member member, Company company) {

        isAdmin(member);

        Optional<Member> findMember = memberRepository.findById(member.getId());

        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CustomException(ExceptionEnum.NOT_EXIST_CATEGORY)
        );

        if (!Objects.equals(findCategory.getCompany().getId(), company.getId())) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_CATEGORY);
        }

        findCategory.update(categoryName.trim());

        return SuccessResponse.of(HttpStatus.OK,"카테고리 수정 완료");
    }

    @Transactional
    public SuccessResponse deleteCategory(Long categoryId, Member member, Company company) {

        isAdmin(member);


        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CustomException(ExceptionEnum.NOT_EXIST_CATEGORY)
        );

        if (!Objects.equals(findCategory.getCompany().getId(), company.getId())) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_CATEGORY);
        }

        categoryRepository.delete(findCategory);

        return SuccessResponse.of(HttpStatus.OK, "카테고리 삭제 완료");
    }

    private static void isAdmin(Member member) {
        if(member.getPosition() != PositionEnum.ADMIN){
            throw new CustomException(ExceptionEnum.REQUIRED_ADMIN_POSITION);
        }
    }
}
