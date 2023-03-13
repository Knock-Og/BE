package com.project.comgle.service;

import com.project.comgle.dto.request.PostRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.Category;
import com.project.comgle.entity.Keyword;
import com.project.comgle.entity.Post;
import com.project.comgle.entity.PostCategory;
import com.project.comgle.repository.CategoryRepository;
import com.project.comgle.repository.PostCategoryRepository;
import com.project.comgle.repository.PostRepository;
import com.project.comgle.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostCategoryRepository postCategoryRepository;

    @Transactional
    public ResponseEntity<MessageResponseDto> createPost(PostRequestDto postRequestDto, UserDetailsImpl userDetails) {

        Post newPost = Post.from(postRequestDto, userDetails.getUser());

        for (String k: postRequestDto.getKeywords()) {
            Keyword keyword = Keyword.of(k);
            keyword.addPost(newPost);
        }

        Category category = new Category(postRequestDto.getCategory());
        categoryRepository.save(category);
        postRepository.save(newPost);

        PostCategory postCategory = PostCategory.of(category, newPost);
        postCategoryRepository.save(postCategory);

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "게시글 작성 성공"));
    }

}
