package com.project.comgle.service;

import com.project.comgle.dto.response.PostResponseDto;
import com.project.comgle.entity.Keyword;
import com.project.comgle.entity.Post;
import com.project.comgle.repository.PostRepository;
import com.project.comgle.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAllMyPosts(UserDetailsImpl userDetails) {
        List<Post> allMyPosts = postRepository.findAllByMember(userDetails.getMember());
        List<PostResponseDto> responseDtos = new ArrayList<>();
        for (Post post : allMyPosts) {
            List<Keyword> keywords = post.getKeywords();
            String[] keyword = new String[keywords.size()];
            for (int i = 0; i < keywords.size(); i++) {
                keyword[i] = keywords.get(i).getKeyword();
            }
            responseDtos.add(PostResponseDto.of(post, post.getCategory().getCategoryName(), keyword));
        }
        return responseDtos;
    }
}