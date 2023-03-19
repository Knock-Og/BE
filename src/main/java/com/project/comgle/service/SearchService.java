package com.project.comgle.service;

import com.project.comgle.dto.response.PostResponseDto;
import com.project.comgle.dto.response.SearchResponseDto;
import com.project.comgle.entity.Keyword;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.Post;
import com.project.comgle.entity.Comment;
import com.project.comgle.repository.CategoryRepository;
import com.project.comgle.repository.CommentRepository;
import com.project.comgle.repository.KeywordRepository;
import com.project.comgle.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PostRepository postRepository;
    private final KeywordRepository keywordRepository;

    private final CommentRepository commentRepository;

    @Transactional
    public List<SearchResponseDto> searchKeyword(String keyword, Member member) {

//        List<Post> findPosts = postRepository.findAllByKeywordsContainsOrderByCreatedAtDesc(keyword);
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
//            List<Comment> commentList = post.getComments();

            searchResponseDtoList.add(SearchResponseDto.of(post, keywordList, commentList.size()));
        }

        return searchResponseDtoList;
    }

}
