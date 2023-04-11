package com.project.comgle.post.service;

import com.project.comgle.comment.entity.Comment;
import com.project.comgle.comment.repository.CommentRepository;
import com.project.comgle.company.entity.Company;
import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.post.dto.SearchPageResponseDto;
import com.project.comgle.post.dto.SearchResponseDto;
import com.project.comgle.post.entity.Keyword;
import com.project.comgle.post.entity.Post;
import com.project.comgle.post.repository.PostRepositoryImpl;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final CommentRepository commentRepository;
    private final PostRepositoryImpl postRepositorys;

    @ExeTimer
    @Transactional
    public SearchPageResponseDto searchKeyword(int page, String keyword, String sortType, Company company) {
//        Page<Post> results = postRepositorys.findAllByContainingKeyword(page, getWords(keyword), sortType, company.getId());
//        int endPage = results.getTotalPages();

//        results.stream().map(k -> {
//            String[] key = k.getKeywords().stream().map(Keyword::getKeyword).toArray(String[]::new);
//            List<Comment> allByPost = commentRepository.findAllByPost(k);
//            return SearchResponseDto.of(k, key, allByPost.size());})
//        .collect(Collectors.toList());

//        return SearchPageResponseDto.of(0, results);

        Set<String> keywordResult = getWords(keyword);

        List<SearchResponseDto> searchResponseDtoList = postRepositorys.findAllByContainingKeyword(page, keywordResult, sortType, company.getId())
                .stream().map(k -> {
                            String[] key = k.getKeywords().stream().map(Keyword::getKeyword).toArray(String[]::new);
                            List<Comment> allByPost = commentRepository.findAllByPost(k);
                            return SearchResponseDto.of(k, key, allByPost.size());
                        }
                ).collect(Collectors.toList());

        int totalCount = postRepositorys.findAllByContainingKeywordCount(keywordResult, company.getId()).size();
        int endPage = (int)(Math.ceil(totalCount/10));
//        int endPage = totalCount%10 != 0 ? totalCount/10+1 : totalCount/10;

        return SearchPageResponseDto.of(endPage, searchResponseDtoList);
    }

    @Transactional
    public SearchPageResponseDto searchCategory(int page, String category, String sortType, Company company) {

        List<SearchResponseDto> searchResponseDtoList = postRepositorys.findAllByContainingCategory(page, category, sortType, company.getId())
                .stream().map(k -> {
                    String[] key = k.getKeywords().stream().map(Keyword::getKeyword).toArray(String[]::new);
                    List<Comment> allByPost = commentRepository.findAllByPost(k);
                    return SearchResponseDto.of(k, key, allByPost.size());}
                ).collect(Collectors.toList());

        int totalCount = postRepositorys.findAllByContainingCategoryCount(category, company.getId()).size();
        int endPage = (int)(Math.ceil(totalCount/10));
//        int endPage = totalCount%10 != 0 ? totalCount/10+1 : totalCount/10;

        return SearchPageResponseDto.of(endPage, searchResponseDtoList);
    }

    // 키워드 명사 추출
    private Set<String> getWords(String keyword) {
        long start = System.currentTimeMillis();

        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);

        KomoranResult result = komoran.analyze(keyword);

        Set<String> nouns = new HashSet<>(result.getNouns());

        log.info("search keywords = {}, time = {}", String.join(", ", nouns), System.currentTimeMillis() - start  + "ms");

        return nouns;
    }

}

