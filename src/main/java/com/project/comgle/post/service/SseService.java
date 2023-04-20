package com.project.comgle.post.service;

import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.post.entity.Post;
import com.project.comgle.post.entity.SseEmitters;
import com.project.comgle.post.repository.EmitterRepository;
import com.project.comgle.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SseService {

    private final EmitterRepository emitterRepository;
    private final PostRepository postRepository;

    public SseEmitter subscribe(Long postId, Long memberId) {

        Optional<Post> findPosts = postRepository.findById(postId);
        if (findPosts.isEmpty()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        SseEmitters postEmitters = emitterRepository.subscibePosts(postId);
        SseEmitter memberEmitter = postEmitters.subscribeMember(memberId);

        try {
            memberEmitter.send(SseEmitter.event().name(findPosts.get().getEditingStatus()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        findPosts.get().updateStatus("true");
        postRepository.save(findPosts.get());

        return memberEmitter;
    }

}
