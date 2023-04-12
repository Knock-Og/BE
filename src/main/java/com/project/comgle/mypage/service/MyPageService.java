package com.project.comgle.mypage.service;

import com.project.comgle.bookmark.repository.BookMarkRepository;
import com.project.comgle.comment.entity.Comment;
import com.project.comgle.comment.repository.CommentRepository;
import com.project.comgle.post.dto.PostPageResponseDto;
import com.project.comgle.member.dto.MemberResponseDto;
import com.project.comgle.member.entity.Member;
import com.project.comgle.member.repository.MemberRepository;
import com.project.comgle.post.dto.PostResponseDto;
import com.project.comgle.post.entity.Keyword;
import com.project.comgle.post.repository.PostRepository;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.post.repository.PostRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final PostRepositoryImpl postRepositorys;
    private final CommentRepository commentRepository;
    private final BookMarkRepository bookMarkRepository;

    @Transactional(readOnly = true)
    public PostPageResponseDto listMyPost(int page, UserDetailsImpl userDetails) {

        List<PostResponseDto> postResponseDtoList = postRepositorys.findAllByMember(page, userDetails.getMember().getId())
                .stream().map(k -> {
                    String[] keywordList = k.getKeywords().stream().map(Keyword::getKeyword).toArray(String[]::new);
                    int commentCount = commentRepository.findAllByPost(k).size();
                    return PostResponseDto.of(k, k.getCategory().getCategoryName(), keywordList, commentCount);}
                ).collect(Collectors.toList());

        int totalCount = postRepositorys.findAllByMemberCount(userDetails.getMember().getId()).size();
        int endPage = totalCount%10 != 0 ? totalCount/10+1 : totalCount/10;

        return PostPageResponseDto.of(endPage, postResponseDtoList);

    }

    @Transactional(readOnly = true)
    public MemberResponseDto myInfo(UserDetailsImpl userDetails) {

        Optional<Member> findMember = memberRepository.findById(userDetails.getMember().getId());

        MemberResponseDto memberResponseDto = MemberResponseDto.from(findMember.get());

        return memberResponseDto;
    }

}