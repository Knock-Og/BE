package com.project.comgle.service;

import com.project.comgle.dto.common.SuccessResponse;
import com.project.comgle.dto.response.BookMarkFolderResponseDto;
import com.project.comgle.dto.response.PostResponseDto;
import com.project.comgle.entity.*;
import com.project.comgle.exception.CustomException;
import com.project.comgle.exception.ExceptionEnum;
import com.project.comgle.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookMarkService {

    private final BookMarkFolderRepository bookMarkFolderRepository;
    private final BookMarkRepository bookMarkRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final KeywordRepository keywordRepository;

    // 즐겨찾기 폴더 추가
    @Transactional
    public SuccessResponse createBookMarkFolder(String folderName, Member member){
        Optional<Member> findMember = memberRepository.findById(member.getId());
        if(findMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        Optional<BookMarkFolder> bookMarkFolder = bookMarkFolderRepository.findByBookMarkFolderNameAndMember(folderName, member);
        if(bookMarkFolder.isPresent()){
            throw new CustomException(ExceptionEnum.DUPLICATE_FOLDER);
        }

        Long countFolder = bookMarkFolderRepository.countAllByMember(member);
        if(countFolder>100){
            throw new CustomException(ExceptionEnum.EXCEED_FOLDER_NUM);
        }

        BookMarkFolder newBookMarkFolder = BookMarkFolder.of(folderName, member);
        bookMarkFolderRepository.save(newBookMarkFolder);

        return SuccessResponse.of(HttpStatus.CREATED, "Add BookMark Folder Successful");
    }

    // 즐겨찾기 폴더 삭제
    @Transactional
    public SuccessResponse delBookMarkFolder(Long folderId, Member member){
        Optional<Member> findMember = memberRepository.findById(member.getId());
        if(findMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        Optional<BookMarkFolder> bookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, member);
        if(bookMarkFolder.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_FOLDER);
        }

        bookMarkFolderRepository.delete(bookMarkFolder.get());

        return SuccessResponse.of(HttpStatus.CREATED, "Delete BookMark Folder Successful");
    }

    // 즐겨찾기 폴더 수정
    @Transactional
    public SuccessResponse updateBookMarkFolder(Long folderId, String modifyFolderName, Member member){
        Optional<Member> findMember = memberRepository.findById(member.getId());
        if(findMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        Optional<BookMarkFolder> bookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, member);
        if(bookMarkFolder.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_FOLDER);
        }

        Optional<BookMarkFolder> findBookMarkFolder = bookMarkFolderRepository.findByBookMarkFolderNameAndMember(modifyFolderName,member);
        if(findBookMarkFolder.isPresent()){
            throw new CustomException(ExceptionEnum.DUPLICATE_FOLDER);
        }

        bookMarkFolder.get().update(modifyFolderName);

        return SuccessResponse.of(HttpStatus.CREATED, "Modifying BookMark Folder Successful");
    }

    // 즐겨찾기 폴더(만) 조회
    @Transactional(readOnly = true)
    public List<BookMarkFolderResponseDto> readBookMarkFolder(Member member){
        Optional<Member> findMember = memberRepository.findById(member.getId());
        if(findMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        List<BookMarkFolder> bookMarkFolders = bookMarkFolderRepository.findAllByMember(member);
        if(bookMarkFolders.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_FOLDER);
        }

        List<BookMarkFolderResponseDto> bookMarkFolderList = new ArrayList<>();

        for(BookMarkFolder bookMarkFolder : bookMarkFolders){
            bookMarkFolderList.add(BookMarkFolderResponseDto.of(bookMarkFolder.getId(), bookMarkFolder.getBookMarkFolderName()));
        }

        return bookMarkFolderList;
    }

    // 즐겨찾기 추가
    @Transactional
    public SuccessResponse postBookMark(Long folderId, Long postId, Member member){
        Optional<Member> findMember = memberRepository.findById(member.getId());
        if(findMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        Optional<BookMarkFolder> findBookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, member);
        if(findBookMarkFolder.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_FOLDER);
        }

        Optional<BookMark> findBookMark = bookMarkRepository.findByBookMarkFolderIdAndPostId(folderId, postId);
        if(findBookMark.isPresent()){
            throw new CustomException(ExceptionEnum.DUPLICATE_POST);
        }

        BookMark newBookMark = BookMark.of(findMember.get(), findPost.get(), findBookMarkFolder.get());
        bookMarkRepository.save(newBookMark);

        findPost.get().updateMethod(WeightEnum.BOOKMARK.getNum());

        return SuccessResponse.of(HttpStatus.CREATED, "Register BookMark");
    }

    // 즐겨찾기 취소
    @Transactional
    public SuccessResponse delBookMark(Long folderId, Long postId, Member member){
        Optional<Member> findMember = memberRepository.findById(member.getId());
        if(findMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        Optional<BookMarkFolder> findBookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, member);
        if(findBookMarkFolder.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_FOLDER);
        }

        Optional<BookMark> bookMark = bookMarkRepository.findByBookMarkFolderIdAndPostId(folderId, postId);

        bookMarkRepository.delete(bookMark.get());

        return SuccessResponse.of(HttpStatus.CREATED, "Unregister BookMark");
    }

    // 즐겨찾기 폴더 별 게시글 조회
    public List<PostResponseDto> readPostForBookMark(Long folderId, Member member){
        Optional<Member> findMember = memberRepository.findById(member.getId());
        if(findMember.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        Optional<BookMarkFolder> findBookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, member);
        if(findBookMarkFolder.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_FOLDER);
        }

        List<BookMark> bookMarkList = bookMarkRepository.findAllByBookMarkFolderId(folderId);
        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        for (int i = 0; i < bookMarkList.size(); i++) {
            Optional<Post> findPost = postRepository.findById(bookMarkList.get(i).getPost().getId());

            List<Keyword> keywords = keywordRepository.findAllByPost(findPost.get());
            String[] keywordList = new String[keywords.size()];
            for (int j = 0; j < keywords.size(); j++) {
                keywordList[j] = keywords.get(j).getKeyword();
            }

            String getCategory = findPost.get().getCategory().getCategoryName();

            postResponseDtoList.add(PostResponseDto.of(findPost.get(), getCategory, keywordList));
        }

        if(postResponseDtoList.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_POST);
        }

        return postResponseDtoList;
    }
}
