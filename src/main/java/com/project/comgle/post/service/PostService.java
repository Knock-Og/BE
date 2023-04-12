package com.project.comgle.post.service;

import com.project.comgle.admin.entity.Category;
import com.project.comgle.admin.repository.CategoryRepository;
import com.project.comgle.bookmark.repository.BookMarkRepository;
import com.project.comgle.comment.entity.Comment;
import com.project.comgle.comment.repository.CommentRepository;
import com.project.comgle.company.entity.Company;
import com.project.comgle.global.common.response.MessageResponseDto;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.global.security.UserDetailsImpl;
import com.project.comgle.member.entity.Member;
import com.project.comgle.post.dto.PostRequestDto;
import com.project.comgle.post.dto.PostResponseDto;
import com.project.comgle.post.entity.*;
import com.project.comgle.post.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final KeywordRepository keywordRepository;
    private final KeywordRepositoryImpl keywordRepositoryImpl;
    private final EmitterRepository emitterRepository;
    private final LogRepository logRepository;
    private final BookMarkRepository bookMarkRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public ResponseEntity<MessageResponseDto> addPost(PostRequestDto postRequestDto, UserDetailsImpl userDetails) {

        Optional<Category> findCategory = categoryRepository.findByCategoryNameAndCompany(postRequestDto.getCategory(), userDetails.getCompany());

        if (findCategory.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_CATEGORY);
        }

        Post newPost = Post.from(postRequestDto, findCategory.get(),userDetails.getMember());
        Set<String> keySet = new HashSet<>(Arrays.asList(postRequestDto.getKeywords()));

        for (String k: keySet) {
            Keyword keyword = Keyword.of(k);
            keyword.addPost(newPost);
        }

        postRepository.save(newPost);

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "Your post has been created successfully."));
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> deletePost(Long id, Member member) {

        Optional<Post> post = postRepository.findByIdAndMember(id, member);

        if (post.isEmpty() || !post.get().isValid()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        post.get().withdrawal();

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "Your post has been deleted successfully."));
    }

    @Transactional
    public ResponseEntity<MessageResponseDto> updatePost(Long id, PostRequestDto postRequestDto, Member member) {

        Optional<Post> findPost = postRepository.findById(id);
        Optional<Category> findCategory = categoryRepository.findByCategoryNameAndCompany(postRequestDto.getCategory(), member.getCompany());

        if (findPost.isEmpty() || !findPost.get().isValid()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        } else if (member.getPosition().getNum() < findPost.get().getModifyPermission().getNum()) {
            throw new CustomException(ExceptionEnum.INVALID_PERMISSION_TO_MODIFY);
        } else if (findCategory.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_CATEGORY);
        }

        List<Keyword> currentKeywords = keywordRepositoryImpl.findAllByPost(findPost.get());
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

        findPost.get().update(postRequestDto, findCategory.get());

        SseEmitters findSubscribingPosts = emitterRepository.subscibePosts(id);

        findSubscribingPosts.getSseEmitters().forEach((postId, emitter) -> {
            try {
                emitter.send(SseEmitter.event().name("Post Modified").data("수정 완료!"));
                emitter.complete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        addLog(member.getMemberName(), findPost.get(), postRequestDto.getContent());

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "Your post has been modified successfully."));
    }

    private void addLog(String memberName, Post post, String newContent) {

        String oldContent = post.getContent();
        List<Integer> changedLineNum = new ArrayList<>();
        String[] oldContentLines = oldContent.split("\n");
        String[] newContentLines = newContent.split("\n");

        for (int i = 0; i < oldContentLines.length; i++) {
            if (!oldContentLines[i].equals(newContentLines[i])) {
                changedLineNum.add(i + 1);
            }
        }

        Log newLog = Log.of(memberName, oldContent, newContent, changedLineNum, post);

        logRepository.save(newLog);
    }

    @Transactional
    public ResponseEntity<PostResponseDto> readPost(Long id, Member member, Company company) {

        Optional<Post> findPost = postRepository.findById(id);

        if (findPost.isEmpty() || !findPost.get().isValid()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        } else if (!Objects.equals(company.getId(), findPost.get().getMember().getCompany().getId())) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST_IN_COMPANY);
        } else if (member.getPosition().getNum() < findPost.get().getReadablePosition().getNum()){
            throw new CustomException(ExceptionEnum.INVALID_PERMISSION_TO_READ);
        }

        Post post = findPost.get();

        List<Keyword> keywords = keywordRepositoryImpl.findAllByPost(post);
        String[] keywordList = keywords.stream().map(Keyword::getKeyword).toArray(String[]::new);
        Integer[] folders = bookMarkRepository.findFoldersByPost(member, post).toArray(Integer[]::new);
        findPost.get().updateMethod(WeightEnum.POSTVIEWS.getNum());
        int commentCount = commentRepository.findAllByPost(post).size();

        int postViews = post.getPostViews();

        return ResponseEntity.ok()
                .body(
                        PostResponseDto.of( findPost.get(),
                                findPost.get().getCategory().getCategoryName(),
                                keywordList,
                                folders,
                                postViews,
                                commentCount)
                );
    }

    // 임시로 editingStatus 수정
    @Transactional
    public ResponseEntity<MessageResponseDto> changeEditingStatus(Long id, Member member) {

        Optional<Post> findPost = postRepository.findById(id);

        if (findPost.isEmpty() ||!findPost.get().isValid()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        } else if (member.getPosition().getNum() < findPost.get().getModifyPermission().getNum()) {
            throw new CustomException(ExceptionEnum.INVALID_PERMISSION_TO_MODIFY);
        }

        findPost.get().updateStatus("true");
        postRepository.save(findPost.get());

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "Edit status changed successfully."));
    }

}
