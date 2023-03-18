package com.project.comgle.service;

import com.project.comgle.dto.request.PostRequestDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.dto.response.PostResponseDto;
import com.project.comgle.entity.Category;
import com.project.comgle.entity.Keyword;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.Post;
import com.project.comgle.repository.*;
import com.project.comgle.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;
    private final CompanyRepository companyRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ResponseEntity<MessageResponseDto> createPost(PostRequestDto postRequestDto, UserDetailsImpl userDetails) {

        Optional<Category> findCategory = categoryRepository.findByCategoryNameAndCompany(postRequestDto.getCategory(), userDetails.getCompany());

        if (findCategory.isEmpty()){
            throw new IllegalArgumentException("카테고리가 유효하지 않습니다.");
        }
        Post newPost = Post.from(postRequestDto, findCategory.get(),userDetails.getUser());

        for (String k: postRequestDto.getKeywords()) {
            Keyword keyword = Keyword.of(k);
            keyword.addPost(newPost);
        }

        postRepository.save(newPost);

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "작성 완료"));
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> deletePost(Long id, Member member) {

        Optional<Post> post = postRepository.findByIdAndMember(id, member);

        if (post.isEmpty()) {
            throw new IllegalArgumentException("해당 게시물이 없습니다.");
        }

        keywordRepository.deleteAllByPost(post.get());

        postRepository.delete(post.get());

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "삭제 완료"));
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> updatePost(Long id, PostRequestDto postRequestDto, Member member) {

        Optional<Post> findPost = postRepository.findById(id);
        Optional<Category> findCategory = categoryRepository.findByCategoryNameAndCompany(postRequestDto.getCategory(), member.getCompany());

        if (findPost.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글이 없습니다.");
        } else if (member.getPosition().getNum() < findPost.get().getModifyPermission().getNum()) {
            throw new IllegalArgumentException("수정 가능한 회원 등급이 아닙니다.");
        } else if (findCategory.isEmpty()){
            throw new IllegalArgumentException("해당 카테고리가 없습니다.");
        }

        List<Keyword> currentKeywords = keywordRepository.findAllByPost(findPost.get());
        List<String> inputKeyWords = new ArrayList<>(Arrays.asList(postRequestDto.getKeywords()));
        List<String> newKeywords = new ArrayList<>();

        for (Keyword k: currentKeywords) {
            if(!inputKeyWords.contains(k.getKeyword())){
                keywordRepository.delete(k);
            }
            newKeywords.add(k.getKeyword());
        }

        for (String s : inputKeyWords) {
            if(!newKeywords.contains(s)){
                Keyword keyword = Keyword.of(s);
                keyword.addPost(findPost.get());
                keywordRepository.save(keyword);
            }
        }

        findPost.get().update(postRequestDto,findCategory.get());

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "수정 완료"));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<PostResponseDto> readPost(Long id, Member member) {

        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new IllegalArgumentException("해당 게시글이 없습니다.");
        }else if(member.getPosition().getNum() < post.get().getReadablePosition().getNum()){
            throw new IllegalArgumentException("읽기 가능한 회원 등급이 아닙니다.");
        }

        List<Keyword> keywords = keywordRepository.findAllByPost(post.get());
        String[] keywordList = new String[keywords.size()];
        for (int i=0; i < keywords.size(); i++) {
            keywordList[i] = keywords.get(i).getKeyword();
        }

        // 댓글은 구현 후 추가 예정

        return ResponseEntity.ok()
                .body(
                        PostResponseDto.of( post.get(),
                                post.get().getCategory().getCategoryName(),
                                keywordList)
                );
    }

}
