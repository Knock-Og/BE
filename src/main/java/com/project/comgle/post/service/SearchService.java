package com.project.comgle.post.service;

import com.project.comgle.comment.repository.CommentRepository;
import com.project.comgle.company.entity.Company;
import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.post.dto.SearchPageResponseDto;
import com.project.comgle.post.dto.SearchResponseDto;
import com.project.comgle.post.entity.Keyword;
import com.project.comgle.post.repository.PostRepositoryImpl;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final CommentRepository commentRepository;
    private final PostRepositoryImpl postRepositorys;

    @ExeTimer
    @Transactional
    public SearchPageResponseDto searchKeyword(int page, String keyword, String sortType, Company company) {

        Set<String> keywordResult = getWords(keyword);

        List<SearchResponseDto> searchResponseDtoList = postRepositorys.findAllByContainingKeyword(page, keywordResult, sortType, company.getId())
                .stream().map(k -> {
                            String[] key = k.getKeywords().stream().map(Keyword::getKeyword).toArray(String[]::new);
                            int commentCount = commentRepository.findAllByPost(k).size();
                            return SearchResponseDto.of(k, key, commentCount);
                        }
                ).collect(Collectors.toList());

        int totalCount = postRepositorys.findAllByContainingKeywordCount(keywordResult, company.getId()).size();
        int endPage = totalCount%10 != 0 ? totalCount/10+1 : totalCount/10;

        return SearchPageResponseDto.of(endPage, searchResponseDtoList);
    }

    @Transactional
    public SearchPageResponseDto searchCategory(int page, String category, String sortType, Company company) {

        List<SearchResponseDto> searchResponseDtoList = postRepositorys.findAllByContainingCategory(page, category, sortType, company.getId())
                .stream().map(k -> {
                    String[] key = k.getKeywords().stream().map(Keyword::getKeyword).toArray(String[]::new);
                    int commentCount = commentRepository.findAllByPost(k).size();
                    return SearchResponseDto.of(k, key, commentCount);}
                ).collect(Collectors.toList());

        int totalCount = postRepositorys.findAllByContainingCategoryCount(category, company.getId()).size();
        int endPage = totalCount%10 != 0 ? totalCount/10+1 : totalCount/10;

        return SearchPageResponseDto.of(endPage, searchResponseDtoList);
    }

    private Set<String> getWords(String keyword) {
        long start = System.currentTimeMillis();

        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);

        KomoranResult result = komoran.analyze(keyword);

        Set<String> nouns = new HashSet<>(result.getNouns());

        log.info("search keywords = {}, time = {}", String.join(", ", nouns), System.currentTimeMillis() - start  + "ms");

        return nouns;
    }

}

