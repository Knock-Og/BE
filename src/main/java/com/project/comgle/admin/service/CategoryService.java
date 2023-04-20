package com.project.comgle.admin.service;

import com.project.comgle.admin.dto.CategoryResponseDto;
import com.project.comgle.admin.entity.Category;
import com.project.comgle.admin.repository.CategoryRepository;
import com.project.comgle.company.entity.Company;
import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.member.entity.Member;
import com.project.comgle.post.entity.Post;
import com.project.comgle.post.repository.PostRepository;
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

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    @Transactional
    public SuccessResponse addCategory(String categoryName, Member member, Company company) {

        Optional<Category> findCategory = categoryRepository.findByCategoryNameAndCompany(categoryName, company);

        if(findCategory.isPresent()){
            if(!findCategory.get().isValid()){
                findCategory.get().restore();
            }else{
                throw new CustomException(ExceptionEnum.DUPLICATE_CATEGORY);
            }
        }else{
            categoryRepository.save(Category.of(categoryName, member.getCompany()));
        }

        return SuccessResponse.of(HttpStatus.OK, "The category has been added successfully.");
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findCategories(Company company) {

        List<Category> categoryList = categoryRepository.findAllByCompany(company);

        return categoryList.stream().map(CategoryResponseDto::from).collect(Collectors.toList());
    }

    @Transactional
    public SuccessResponse modifyCategory(Long categoryId, String categoryName, Company company) {

        Optional<Category> findCategory = categoryRepository.findById(categoryId);

        if (findCategory.isEmpty() || !findCategory.get().isValid()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_CATEGORY);
        } else if (!Objects.equals(findCategory.get().getCompany().getId(), company.getId())) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_CATEGORY);
        }

        findCategory.get().update(categoryName);

        return SuccessResponse.of(HttpStatus.OK,"The category has been modified successfully.");
    }

    @Transactional
    public SuccessResponse deleteCategory(Long categoryId, Company company) {

        Optional<Category> findCategory = categoryRepository.findById(categoryId);

        if (findCategory.isEmpty() || !findCategory.get().isValid()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_CATEGORY);
        } else if (!Objects.equals(findCategory.get().getCompany().getId(), company.getId())) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_CATEGORY);
        }

        List<Post> posts = postRepository.findAllByCategory(findCategory.get());

        posts.forEach(Post::withdrawal);

        findCategory.get().withdrawal();

        return SuccessResponse.of(HttpStatus.OK, "The category has been deleted successfully.");
    }

}
