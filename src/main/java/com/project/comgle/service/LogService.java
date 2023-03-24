package com.project.comgle.service;

import com.project.comgle.dto.response.LogResponseDto;
import com.project.comgle.entity.Log;
import com.project.comgle.entity.Post;
import com.project.comgle.exception.CustomException;
import com.project.comgle.exception.ExceptionEnum;
import com.project.comgle.repository.LogRepository;
import com.project.comgle.repository.PostRepository;
import com.project.comgle.security.UserDetailsImpl;
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