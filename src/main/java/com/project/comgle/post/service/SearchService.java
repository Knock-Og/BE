package com.project.comgle.post.service;

import com.project.comgle.admin.entity.Category;
import com.project.comgle.company.entity.Company;
import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.member.entity.Member;
import com.project.comgle.post.dto.SearchPageResponseDto;
import com.project.comgle.post.dto.SearchResponseDto;
import com.project.comgle.comment.entity.Comment;
import com.project.comgle.post.entity.Keyword;
import com.project.comgle.post.entity.Post;
import com.project.comgle.admin.repository.CategoryRepository;
import com.project.comgle.comment.repository.CommentRepository;
import com.project.comgle.post.repository.KeywordRepository;
import com.project.comgle.post.repository.KeywordRepositoryImpl;
import com.project.comgle.post.repository.PostRepository;
import com.project.comgle.post.repository.PostRepositoryImpl;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final PostRepositoryImpl postRepositoryImpl;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepositoryImpl postRepositorys;

    @ExeTimer
    @Transactional
    public List<SearchResponseDto> searchKeyword(String keyword, Company company) {


        return postRepositorys.findAllByContainingKeyword(getWords(keyword),company.getId())
                .stream().map(k -> {
                            String[] key = k.getKeywords().stream().map(Keyword::getKeyword).toArray(String[]::new);
                            List<Comment> allByPost = commentRepository.findAllByPost(k);
                            return SearchResponseDto.of(k, key, allByPost.size()); }
                ).collect(Collectors.toList());
    }

/*
        List<String> keywords = getWords(keyword);
        List<Post> postList = new ArrayList<>();

        for (String key : keywords) {
            postList.addAll(postRepositoryImpl.findAllByContainingKeyword(key));
        }

        List<Post> companyPost = postList.stream().distinct().filter(p -> Objects.equals(p.getCategory().getCompany().getId(), company.getId()))
                .sorted(Comparator.comparingInt(Post::getScore).reversed()).collect(Collectors.toList());

        List<SearchResponseDto> searchResponseDtoList = new ArrayList<>();
        for (Post post : companyPost) {
            List<Keyword> keywords2 = post.getKeywords();
            String[] keywordList = new String[keywords2.size()];
            for (int i = 0; i < keywords2.size(); i++) {
                keywordList[i] = keywords2.get(i).getKeyword();
            }

            List<Comment> commentList = commentRepository.findAllByPost(post);
*/





    @Transactional
    public SearchPageResponseDto searchCategory(String category, int page, Member member) {

        Optional<Category> findCategory = categoryRepository.findByCategoryNameAndCompany(category, member.getCompany());
        if (findCategory.isEmpty()) {
            throw new IllegalArgumentException("해당 카테고리의 게시물이 없습니다.");
        }

        int nowPage = page-1;   // 현재 페이지
        int size = 10;  // 한 페이지당 게시글 수

        int endP = postRepository.countByCategoryId(findCategory.get().getId());
        if(endP % size == 0){
            endP = endP / size;
        } else if (endP % size > 0) {
            endP = endP / size + 1;
        }

        Page<Post> findPosts = postRepository.findAllByCategoryId(findCategory.get().getId(), PageRequest.of(nowPage, size));

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

        if(searchResponseDtoList.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        return  SearchPageResponseDto.of(endP,searchResponseDtoList);
    }

    // 키워드 명사 추출


    public List<String> getWords(String keyword) {
        long start = System.currentTimeMillis();

        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);

        KomoranResult result = komoran.analyze(keyword);

        List<String> nouns = result.getNouns();
        
        log.info("search keywords = {}, time = {}", String.join(", ", nouns), System.currentTimeMillis() - start  + "ms";

        return nouns;
    }

}

