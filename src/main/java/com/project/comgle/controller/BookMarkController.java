package com.project.comgle.controller;

import com.project.comgle.dto.request.BookMarkFolderRequestDto;
import com.project.comgle.dto.response.BookMarkFolderResponseDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.BookMarkFolder;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.BookMarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookMarkController {
    private final BookMarkService bookMarkService;

    // 즐겨찾기 폴더 추가
    @PostMapping("/book-marks/folders")
    public ResponseEntity<MessageResponseDto> createBookMarkFolder(@RequestBody BookMarkFolderRequestDto bookMarkFolderRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.createBookMarkFolder(bookMarkFolderRequestDto.getBookMarkFolderName(), userDetails.getMember());
    }

    // 즐겨찾기 폴더 삭제
    @DeleteMapping("/book-marks/folders")
    public ResponseEntity<MessageResponseDto> delBookMarkFolder(@RequestParam(name = "bf") String folderName, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.delBookMarkFolder(folderName, userDetails.getMember());
    }

    // 즐겨찾기 폴더(만) 조회
    @GetMapping("/book-marks/folders")
    public List<String> readBookMarkFolder(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.readBookMarkFolder(userDetails.getMember());
    }

    // 즐겨찾기 추가
    @PostMapping("/book-marks/{post-id}")
    public ResponseEntity<MessageResponseDto> postBookMark(@PathVariable(name = "post-id") Long postId, @RequestBody BookMarkFolderRequestDto bookMarkFolderRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.postBookMark(postId, bookMarkFolderRequestDto.getBookMarkFolderName(), userDetails.getMember());
    }

    //즐겨찾기 취소
    @DeleteMapping("/book-marks/{post-id}")
    public ResponseEntity<MessageResponseDto> delBookMark(@PathVariable(name = "post-id") Long postId, @RequestBody BookMarkFolderRequestDto bookMarkFolderRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.delBookMark(postId, bookMarkFolderRequestDto.getBookMarkFolderName(), userDetails.getMember());
    }
}
