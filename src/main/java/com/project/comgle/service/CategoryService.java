package com.project.comgle.service;

import com.project.comgle.dto.response.CategoryResponseDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.Category;
import com.project.comgle.entity.Company;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import com.project.comgle.repository.CategoryRepository;
import com.project.comgle.repository.CompanyRepository;
import com.project.comgle.repository.MemberRepository;
import com.project.comgle.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final CompanyRepository companyRepository;

    @Transactional
    public MessageResponseDto create(String categoryName, Member member,Company company) {

        if(member.getPosition() != PositionEnum.ADMIN){
            throw new IllegalArgumentException("ADMIN 권한이 필요합니다");
        }

        Optional<Category> findCategory = categoryRepository.findByCategoryNameAndCompany(categoryName, company);

        if(findCategory.isPresent()){
            throw new IllegalArgumentException("해당 카테고리가 이미 존재 합니다.");
        }

        categoryRepository.save(new Category(categoryName, member.getCompany()));

        return MessageResponseDto.of(HttpStatus.OK.value(), "카테고리 추가 완료");

    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findCategories(Company company) {

        List<Category> categoryList = categoryRepository.findAllByCompany(company);
        List<CategoryResponseDto> categoryResponseDtos = new ArrayList<>();

        for (Category category : categoryList) {
            categoryResponseDtos.add(CategoryResponseDto.from(category));
        }

        return categoryResponseDtos;
    }

    @Transactional
    public MessageResponseDto updateCategory(Long categoryId, String categoryName, UserDetailsImpl userDetails) {

        Optional<Member> findMember = memberRepository.findById(userDetails.getMember().getId());

        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalStateException("해당 카테고리가 존재하지 않습니다.")
        );

        if(userDetails.getMember().getPosition() != PositionEnum.ADMIN){
            throw new IllegalArgumentException("ADMIN 계정만 가능합니다.");
        } else if (findCategory.getCompany() != findMember.get().getCompany()) {
            throw new IllegalArgumentException("해당 카테고리를 소유한 회사가 아닙니다.");
        }

        findCategory.update(categoryName.trim());

        return MessageResponseDto.of(HttpStatus.OK.value(),"카테고리 수정 완료");
    }

    @Transactional
    public MessageResponseDto deleteCategory(Long categoryId, Member member) {

        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalStateException("해당 카테고리가 존재하지 않습니다.")
        );

        if(member.getPosition() != PositionEnum.ADMIN){
            throw new IllegalArgumentException("ADMIN 계정만 가능합니다.");
        }

//        postCategoryRepository.deleteAllByCategory(findCategory);
        categoryRepository.delete(findCategory);

        return MessageResponseDto.of(HttpStatus.OK.value(), "카테고리 삭제 완료");
    }
}
