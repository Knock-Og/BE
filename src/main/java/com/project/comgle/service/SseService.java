package com.project.comgle.service;

import com.project.comgle.entity.Post;
import com.project.comgle.entity.SseEmitters;
import com.project.comgle.repository.EmitterRepository;
import com.project.comgle.repository.PostRepository;
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
            throw new IllegalArgumentException("해당 게시글이 없습니다.");
        }

        SseEmitters postEmitters = emitterRepository.subscibePosts(postId);
        SseEmitter memberEmitter = postEmitters.subscribeMember(memberId);

        try {
            memberEmitter.send(SseEmitter.event().name(findPosts.get().getEditingStatus()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        findPosts.get().updateStatus("true"); // true = 수정 중
        postRepository.save(findPosts.get());

        return memberEmitter;
    }
}
