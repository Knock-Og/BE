package com.project.comgle.service;

import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.Category;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import com.project.comgle.repository.CategoryRepository;
import com.project.comgle.repository.MemberRepository;
import com.project.comgle.repository.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final PostCategoryRepository postCategoryRepository;

    @Transactional
    public MessageResponseDto create(String categoryName, Member member) {

        if(member.getPosition() != PositionEnum.ADMIN){
            throw new IllegalArgumentException("ADMIN 권한이 필요합니다");
        }

        Optional<Category> findCategory = categoryRepository.findByCategoryName(categoryName);

        if(findCategory.isPresent()){
            throw new IllegalArgumentException("해당 카테고리가 이미 존재 합니다.");
        }

        categoryRepository.save(new Category(categoryName, member.getCompany()));

        return MessageResponseDto.of(HttpStatus.OK.value(), "카테고리 추가 완료");

    }

    @Transactional
    public MessageResponseDto updateCategory(Long categoryId, String categoryName, Member member) {

        Optional<Member> findMember = memberRepository.findById(member.getId());
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(
                () -> new IllegalStateException("해당 카테고리가 존재하지 않습니다.")
        );

        if(member.getPosition() != PositionEnum.ADMIN){
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

        postCategoryRepository.deleteAllByCategory(findCategory);
        categoryRepository.delete(findCategory);

        return MessageResponseDto.of(HttpStatus.OK.value(), "카테고리 삭제 완료");


    }


}
