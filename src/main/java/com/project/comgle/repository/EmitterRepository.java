package com.project.comgle.repository;

import com.project.comgle.entity.SseEmitters;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {
    public static Map<Long, SseEmitters> postSseEmitters = new ConcurrentHashMap<>();

    public SseEmitters subscibePosts(Long postId) {
        if (postSseEmitters.get(postId) == null) {
            SseEmitters postSseEmittersMap = new SseEmitters();
            postSseEmitters.put(postId, postSseEmittersMap);
        }

        return postSseEmitters.get(postId);
    }
}
