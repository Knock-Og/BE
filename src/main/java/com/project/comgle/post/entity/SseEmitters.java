package com.project.comgle.post.entity;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Component
@Slf4j
public class SseEmitters {
    private Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    public SseEmitter subscribeMember(Long memberId) {
        SseEmitter sseEmitter = new SseEmitter(1000L * 60 * 60);
        sseEmitters.put(memberId, sseEmitter);

        sseEmitter.onCompletion(() -> sseEmitters.remove(memberId));
        sseEmitter.onTimeout(() -> sseEmitters.remove(memberId));

        return sseEmitter;
    }

}
