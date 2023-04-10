package com.project.comgle.mypage.service;

import com.project.comgle.bookmark.dto.PostPageResponseDto;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.member.dto.MemberResponseDto;
import com.project.comgle.member.entity.Member;
import com.project.comgle.member.repository.MemberRepository;
import com.project.comgle.post.dto.PostResponseDto;
import com.project.comgle.post.entity.Keyword;
import com.project.comgle.post.entity.Post;
import com.project.comgle.post.repository.PostRepository;
import com.project.comgle.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public PostPageResponseDto listMyPost(int page, UserDetailsImpl userDetails) {

        int nowPage = page-1;   // 현재 페이지
        int size = 10;  // 한 페이지당 게시글 수

        List<Post> allMyPosts = postRepository.findAllByMember(userDetails.getMember(), PageRequest.of(nowPage, size)).toList();

        if(allMyPosts.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        int endP = postRepository.countByMember(userDetails.getMember());
        if(endP % size == 0){
            endP = endP / size;
        } else if (endP % size > 0) {
            endP = endP / size + 1;
        }

        List<PostResponseDto> responseDtos = new ArrayList<>();
        for (Post post : allMyPosts) {
            List<Keyword> keywords = post.getKeywords();
            String[] keyword = new String[keywords.size()];
            for (int i = 0; i < keywords.size(); i++) {
                keyword[i] = keywords.get(i).getKeyword();
            }
            responseDtos.add(PostResponseDto.of(post, post.getCategory().getCategoryName(), keyword));
        }

        if(responseDtos.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        return PostPageResponseDto.of(endP,responseDtos);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto myInfo(UserDetailsImpl userDetails) {

        Optional<Member> findMember = memberRepository.findById(userDetails.getMember().getId());

        MemberResponseDto memberResponseDto = MemberResponseDto.from(findMember.get());

        return memberResponseDto;
    }

}