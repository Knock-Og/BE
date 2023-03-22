package com.project.comgle.controller;

import com.project.comgle.dto.common.SuccessResponse;
import com.project.comgle.dto.request.BookMarkFolderRequestDto;
import com.project.comgle.dto.response.BookMarkFolderResponseDto;
import com.project.comgle.dto.response.PostResponseDto;
import com.project.comgle.security.UserDetailsImpl;
import com.project.comgle.service.BookMarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "BOOKMARK", description = "북마크 관련 API Document")
public class BookMarkController {
    private final BookMarkService bookMarkService;

    @Operation(summary = "즐겨찾기 폴더 추가 API", description = "해당 MEMBER의 즐겨찾기 폴더를 추가합니다. 폴더는 최대 100개까지 가능합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/bookmark/folders")
    public SuccessResponse createBookMarkFolder(@RequestBody BookMarkFolderRequestDto bookMarkFolderRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.createBookMarkFolder(bookMarkFolderRequestDto.getBookMarkFolderName(), userDetails.getMember());
    }

    @Operation(summary = "즐겨찾기 폴더 삭제 API", description = "해당 MEMBER의 즐겨찾기 폴더를 삭제합니다")
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/bookmark/folders/{folder-id}")
    public SuccessResponse delBookMarkFolder(@PathVariable(name = "folder-id") Long folderId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.delBookMarkFolder(folderId, userDetails.getMember());
    }

    @Operation(summary = "즐겨찾기 폴더 수정 API", description = "해당 MEMBER가 지정한 즐겨찾기 폴더명을수정합니다")
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/bookmark/folders/{folder-id}")
    public SuccessResponse updateBookMarkFolder(@PathVariable(name = "folder-id") Long folderId, @RequestBody BookMarkFolderRequestDto bookMarkFolderRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.updateBookMarkFolder(folderId, bookMarkFolderRequestDto.getBookMarkFolderName(), userDetails.getMember());
    }

    @Operation(summary = "즐겨찾기 폴더 전체 조회 API", description = "해당 MEMBER가 생성한 모든 폴더를 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/bookmark/folders")
    public List<BookMarkFolderResponseDto> readBookMarkFolder(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.readBookMarkFolder(userDetails.getMember());
    }

    @Operation(summary = "즐겨찾기 추가 API", description = "해당 폴더에 즐겨찾기를 추가합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/bookmark/folders/{folder-id}/bookmarks/{post-id}")
    public SuccessResponse postBookMark(@PathVariable(name = "folder-id") Long folderId, @PathVariable(name = "post-id") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.postBookMark(folderId, postId, userDetails.getMember());
    }

    @Operation(summary = "즐겨찾기 취소 API", description = "해당 폴더에 즐겨찾기를 취소합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @DeleteMapping("/bookmark/folders/{folder-id}/bookmarks/{post-id}")
    public SuccessResponse delBookMark(@PathVariable(name = "folder-id") Long folderId, @PathVariable(name = "post-id") Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.delBookMark(folderId, postId, userDetails.getMember());
    }

    @Operation(summary = "즐겨찾기 폴더 별 게시글 조회 API", description = "해당 폴더에 즐겨찾기한 게시글을 모두 조회합니다.")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping("/bookmark/folders/{folder-id}/bookmarks")
    public List<PostResponseDto> readPostForBookMark(@PathVariable(name = "folder-id") Long folderId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return bookMarkService.readPostForBookMark(folderId, userDetails.getMember());
    }
}
