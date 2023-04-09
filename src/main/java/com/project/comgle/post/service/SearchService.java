package com.project.comgle.post.service;

import com.project.comgle.comment.entity.Comment;
import com.project.comgle.comment.repository.CommentRepository;
import com.project.comgle.company.entity.Company;
import com.project.comgle.global.aop.ExeTimer;
import com.project.comgle.post.dto.SearchResponseDto;
import com.project.comgle.post.entity.Keyword;
import com.project.comgle.post.repository.PostRepositoryImpl;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final CommentRepository commentRepository;
    private final PostRepositoryImpl postRepositorys;

    @ExeTimer
    @Transactional
    public List<SearchResponseDto> searchKeyword(int page, String keyword, String sortType, Company company) {

        return postRepositorys.findAllByContainingKeyword(page, getWords(keyword), sortType, company.getId())
                .stream().map(k -> {
                            String[] key = k.getKeywords().stream().map(Keyword::getKeyword).toArray(String[]::new);
                            List<Comment> allByPost = commentRepository.findAllByPost(k);
                            return SearchResponseDto.of(k, key, allByPost.size());}
                ).collect(Collectors.toList());
    }

    @Transactional
    public List<SearchResponseDto> searchCategory(int page, String category, String sortType, Company company) {

        return postRepositorys.findAllByContainingCategory(page, category, sortType, company.getId())
                .stream().map(k -> {
                    String[] key = k.getKeywords().stream().map(Keyword::getKeyword).toArray(String[]::new);
                    List<Comment> allByPost = commentRepository.findAllByPost(k);
                    return SearchResponseDto.of(k, key, allByPost.size());}
                ).collect(Collectors.toList());

    }

    // 키워드 명사 추출
    public List<String> getWords(String keyword) {
        long start = System.currentTimeMillis();

        Komoran komoran = new Komoran(DEFAULT_MODEL.LIGHT);

        KomoranResult result = komoran.analyze(keyword);

        List<String> nouns = result.getNouns();

        log.info("search keywords = {}, time = {}", String.join(", ", nouns), System.currentTimeMillis() - start  + "ms");

        return nouns;
    }

}

