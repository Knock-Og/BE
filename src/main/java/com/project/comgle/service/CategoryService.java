package com.project.comgle.service;

import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.Category;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.enumSet.PositionEnum;
import com.project.comgle.repository.CategoryRepository;
import com.project.comgle.repository.MemberRepository;
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

    @Transactional
    public MessageResponseDto create(String categoryName, Member member) {

        if(member.getPosition() != PositionEnum.ADMIN){
            throw new IllegalArgumentException("ADMIN 권한이 필요합니다");
        }

        Optional<Category> findCategory = categoryRepository.findByCategoryName(categoryName);

        if(findCategory.isPresent()){
            throw new IllegalArgumentException("해당 카테고리가 이미 존재 합니다.");
        }

        categoryRepository.save(new Category(categoryName));

        return MessageResponseDto.of(HttpStatus.OK.value(), "카테고리 추가 완료");

    }
}
