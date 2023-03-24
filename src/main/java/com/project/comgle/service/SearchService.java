package com.project.comgle.service;

import com.project.comgle.dto.response.SearchResponseDto;
import com.project.comgle.entity.*;

import com.project.comgle.repository.CategoryRepository;
import com.project.comgle.repository.CommentRepository;
import com.project.comgle.repository.KeywordRepository;
import com.project.comgle.repository.PostRepository;

import com.project.comgle.repository.*;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final PostRepository postRepository;
    private final KeywordRepository keywordRepository;

    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final BookMarkRepository bookMarkRepository;

    @Transactional
    public List<SearchResponseDto> searchKeyword(String keyword, Company company) {

        List<String> keywords = getWords(keyword);
        List<Post> postList = new ArrayList<>();

        // title, content, keyword가 포함 된 게시글 찾기
        for (String key : keywords) {
            postList.addAll(postRepository.findAllByTitleContainsOrContentContaining(key, key));
            List<Keyword> findKeyWords = keywordRepository.findAllByKeywordContains(key);
            for (Keyword k : findKeyWords) {
                postList.add(k.getPost());
            }
        }

        log.info("list post size = {}", postList.size());
        Set<Post> allPosts = new HashSet<>(postList);
        log.info("set post size = {}", allPosts.size());

        // 해당 회사 게시글만 조회하기
        List<Post> companyPost = allPosts.stream().filter(p -> Objects.equals(p.getCategory().getCompany().getId(), company.getId())).collect(Collectors.toList());
        log.info("filter company post size = {}", companyPost.size());
        Collections.sort(companyPost, Comparator.comparingInt(Post::getScore).reversed());

        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();
        for (Post post : companyPost) {
            List<Keyword> keywords2 = post.getKeywords();
            String[] keywordList = new String[keywords2.size()];
            for (int i = 0; i < keywords2.size(); i++) {
                keywordList[i] = keywords2.get(i).getKeyword();
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
        Collections.sort(findPosts, Comparator.comparingInt(Post::getScore).reversed());

        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();
        for (Post post : findPosts) {
            List<Keyword> keywords = post.getKeywords();
            String[] keywordList = new String[keywords.size()];
            for (int i = 0; i < keywords.size(); i++) {
                keywordList[i] = keywords.get(i).getKeyword();
            }

            List<Comment> commentList = commentRepository.findAllByPost(post);

            searchResponseDtoList.add(SearchResponseDto.of(post, keywordList, commentList.size()));
        }

        return searchResponseDtoList;
    }

    // 키워드 명사 추출
    private static List<String> getWords(String keyword) {

        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        KomoranResult result = komoran.analyze(keyword);

        List<String> nouns = result.getNouns();
        
        log.info("search keywords = {}", String.join(", ", nouns));
        return nouns;
    }


}

