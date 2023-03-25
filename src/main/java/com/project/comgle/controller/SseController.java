package com.project.comgle.controller;

import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseController {
    private final SseService sseService;

    @GetMapping(value = "/connect/{post-id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subScribe(@PathVariable("post-id") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return sseService.subscribe(postId, userDetails.getMember().getId());
    }
}
