package com.project.comgle.controller;

import com.project.comgle.dto.request.BookMarkFolderRequestDto;
import com.project.comgle.dto.response.BookMarkFolderResponseDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.dto.response.PostResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.BookMarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "BOOKMARK", description = "북마크 관련 API Document")
public class BookMarkController {
    private final BookMarkService bookMarkService;

    // 즐겨찾기 폴더 추가
    @PostMapping("/bookmark/folders")
    public ResponseEntity<MessageResponseDto> createBookMarkFolder(@RequestBody BookMarkFolderRequestDto bookMarkFolderRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.createBookMarkFolder(bookMarkFolderRequestDto.getBookMarkFolderName(), userDetails.getMember());
    }

    // 즐겨찾기 폴더 삭제
    @DeleteMapping("/bookmark/folders/{folder-id}")
    public ResponseEntity<MessageResponseDto> delBookMarkFolder(@PathVariable(name = "folder-id") Long folderId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.delBookMarkFolder(folderId, userDetails.getMember());
    }

    // 즐겨찾기 폴더 수정
    @PutMapping("/bookmark/folders/{folder-id}")
    public ResponseEntity<MessageResponseDto> updateBookMarkFolder(@PathVariable(name = "folder-id") Long folderId, @RequestBody BookMarkFolderRequestDto bookMarkFolderRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.updateBookMarkFolder(folderId, bookMarkFolderRequestDto.getBookMarkFolderName(), userDetails.getMember());
    }

    // 즐겨찾기 폴더(만) 조회
    @GetMapping("/bookmark/folders")
    public List<BookMarkFolderResponseDto> readBookMarkFolder(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.readBookMarkFolder(userDetails.getMember());
    }

    // 즐겨찾기 추가
    @PostMapping("/bookmark/folders/{folder-id}/bookmarks/{post-id}")
    public ResponseEntity<MessageResponseDto> postBookMark(@PathVariable(name = "folder-id") Long folderId, @PathVariable(name = "post-id") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.postBookMark(folderId, postId, userDetails.getMember());
    }

    //즐겨찾기 취소
    @DeleteMapping("/bookmark/folders/{folder-id}/bookmarks/{post-id}")
    public ResponseEntity<MessageResponseDto> delBookMark(@PathVariable(name = "folder-id") Long folderId, @PathVariable(name = "post-id") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.delBookMark(folderId, postId, userDetails.getMember());
    }

    //즐겨찾기 폴더 별 게시글 조회
    @GetMapping("/bookmark/folders/{folder-id}/bookmarks")
    public List<PostResponseDto> readPostForBookMark(@PathVariable(name = "folder-id") Long folderId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.readPostForBookMark(folderId, userDetails.getMember());
    }
}
