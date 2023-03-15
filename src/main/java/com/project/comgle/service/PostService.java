package com.project.comgle.service;

import com.project.comgle.dto.request.PostRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.*;
import com.project.comgle.entity.enumSet.PositionEnum;
import com.project.comgle.repository.CategoryRepository;
import com.project.comgle.repository.KeywordRepository;
import com.project.comgle.repository.PostCategoryRepository;
import com.project.comgle.repository.PostRepository;
import com.project.comgle.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final KeywordRepository keywordRepository;

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

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "작성 완료"));
    }


    @Transactional
    public ResponseEntity<MessageResponseDto> deletePost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글이 없습니다.");
        }

        List<Keyword> keywordList = keywordRepository.findAllByPost(post.get());
        for (Keyword k : keywordList) {
            keywordRepository.delete(k);
        }

        Optional<PostCategory> postCategory = postCategoryRepository.findById(post.get().getId());
        if(postCategory.isPresent()){
            postCategoryRepository.delete(postCategory.get());
            categoryRepository.deleteById(postCategory.get().getCategory().getId());
        }
        postRepository.delete(post.get());

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "삭제 완료"));
    }

}
