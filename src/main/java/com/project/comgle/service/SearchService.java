package com.project.comgle.service;

import com.project.comgle.dto.response.SearchResponseDto;
import com.project.comgle.entity.*;
import com.project.comgle.repository.CategoryRepository;
import com.project.comgle.repository.CommentRepository;
import com.project.comgle.repository.KeywordRepository;
import com.project.comgle.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PostRepository postRepository;
    private final KeywordRepository keywordRepository;

    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public List<SearchResponseDto> searchKeyword(String keyword, Member member) {

        List<Keyword> findKeyword = keywordRepository.findAllByKeyword(keyword);
        List<Post> findPosts = new ArrayList<>();

        for (Keyword k : findKeyword) {
           findPosts.add(k.getPost());
        }
        if (findPosts.isEmpty()) {
            throw new IllegalArgumentException("해당 키워드의 게시글이 없습니다.");
        }

        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();
        for (Post post : findPosts) {
            List<Keyword> keywords = post.getKeywords();
            String[] keywordList = new String[keywords.size()];
            for (int i=0; i < keywords.size(); i++) {
                keywordList[i] = keywords.get(i).getKeyword();
            }

            List<Comment> commentList = commentRepository.findAllByPost(post);

            searchResponseDtoList.add(SearchResponseDto.of(post, keywordList, commentList.size()));
        }

        return searchResponseDtoList;
    }

    @Transactional
    public List<SearchResponseDto> searchCategory(String category, Member member) {

        Optional<Category> findCategory = categoryRepository.findByCategoryNameAndCompany(category, member.getCompany());
        if (findCategory.isEmpty()) {
            throw new IllegalArgumentException("해당 카테고리의 게시물이 없습니다.");
        }
        List<Post> findPosts = postRepository.findAllByCategoryId(findCategory.get().getId());


        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();
        for (Post post : findPosts) {
            List<Keyword> keywords = post.getKeywords();
            String[] keywordList = new String[keywords.size()];
            for (int i=0; i < keywords.size(); i++) {
                keywordList[i] = keywords.get(i).getKeyword();
            }

            List<Comment> commentList = commentRepository.findAllByPost(post);

            searchResponseDtoList.add(SearchResponseDto.of(post, keywordList, commentList.size()));
        }

        return searchResponseDtoList;
    }

}

