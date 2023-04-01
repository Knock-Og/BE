package com.project.comgle.post.service;

import com.project.comgle.post.dto.LogResponseDto;
import com.project.comgle.post.entity.Log;
import com.project.comgle.post.entity.Post;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.post.repository.LogRepository;
import com.project.comgle.post.repository.PostRepository;
import com.project.comgle.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<LogResponseDto> getAllLogs(Long postId, UserDetailsImpl userDetails) {

        Optional<Post> findPost = postRepository.findById(postId);
        if (findPost.isEmpty()) {
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        List<Log> findLogs = logRepository.findAllByPostOrderByCreateDateDesc(findPost.get());

        List<LogResponseDto> responseDtos = new ArrayList<>();
        for (Log log : findLogs) {
            responseDtos.add(LogResponseDto.from (log));
        }
        return responseDtos;
    }

}