package com.project.comgle.service;

import com.project.comgle.dto.response.BookMarkFolderResponseDto;
import com.project.comgle.dto.response.MessageResponseDto;
import com.project.comgle.entity.BookMark;
import com.project.comgle.entity.BookMarkFolder;
import com.project.comgle.entity.Member;
import com.project.comgle.entity.Post;
import com.project.comgle.repository.BookMarkFolderRepository;
import com.project.comgle.repository.BookMarkRepository;
import com.project.comgle.repository.MemberRepository;
import com.project.comgle.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 즐겨찾기 폴더 추가
    @Transactional
    public ResponseEntity<MessageResponseDto> createBookMarkFolder(String folderName, Member member){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버가 없습니다.")
        );

        Optional<BookMarkFolder> bookMarkFolder = bookMarkFolderRepository.findByBookMarkFolderNameAndMember(folderName, member);
        if(bookMarkFolder.isPresent()){
            throw new IllegalArgumentException("해당 폴더가 이미 존재합니다.");
        }

        Long countFolder = bookMarkFolderRepository.countAllByMember(member);
        if(countFolder>100){
            throw new IllegalArgumentException("최대 폴더 갯수를 초과하였습니다.");
        }

        BookMarkFolder newBookMarkFolder = BookMarkFolder.of(folderName, member);
        bookMarkFolderRepository.save(newBookMarkFolder);

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "즐겨찾기 폴더 추가"));
    }

    // 즐겨찾기 폴더 삭제
    @Transactional
    public ResponseEntity<MessageResponseDto> delBookMarkFolder(Long folderId, Member member){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버가 없습니다.")
        );

        Optional<BookMarkFolder> bookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, findMember);
        if(bookMarkFolder.isEmpty()){
            throw new IllegalArgumentException("해당 폴더가 존재하지 않습니다.");
        }

        bookMarkFolderRepository.delete(bookMarkFolder.get());

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "즐겨찾기 폴더 삭제"));
    }

    // 즐겨찾기 폴더 수정
    @Transactional
    public ResponseEntity<MessageResponseDto> updateBookMarkFolder(Long folderId, String modifyFolderName, Member member){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버가 없습니다.")
        );

        Optional<BookMarkFolder> bookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, findMember);
        if(bookMarkFolder.isEmpty()){
            throw new IllegalArgumentException("해당 폴더가 존재하지 않습니다.");
        }

        Optional<BookMarkFolder> findBookMarkFolder = bookMarkFolderRepository.findByBookMarkFolderNameAndMember(modifyFolderName,findMember);
        if(findBookMarkFolder.isPresent()){
            throw new IllegalArgumentException("중복된 이름의 폴더가 존재합니다.");
        }

        bookMarkFolder.get().update(modifyFolderName);

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "즐겨찾기 폴더 수정 완료"));
    }

    // 즐겨찾기 폴더(만) 조회
    @Transactional(readOnly = true)
    public List<BookMarkFolderResponseDto> readBookMarkFolder(Member member){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버가 없습니다.")
        );

        List<BookMarkFolder> bookMarkFolders = bookMarkFolderRepository.findAllByMember(findMember);
        if(bookMarkFolders.isEmpty()){
            throw new IllegalArgumentException("폴더가 존재하지 않습니다.");
        }

        List<BookMarkFolderResponseDto> bookMarkFolderList = new ArrayList<>();

        for(BookMarkFolder bookMarkFolder : bookMarkFolders){
            bookMarkFolderList.add(BookMarkFolderResponseDto.of(bookMarkFolder.getId(), bookMarkFolder.getBookMarkFolderName()));
        }

        return bookMarkFolderList;
    }

    // 즐겨찾기 추가
    @Transactional
    public ResponseEntity<MessageResponseDto> postBookMark(Long folderId, Long postId, Member member){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버가 없습니다.")
        );

        Optional<Post> findPost = postRepository.findById(postId);
        if(findPost.isEmpty()){
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다.");
        }

        Optional<BookMarkFolder> findBookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, findMember);
        if(findBookMarkFolder.isEmpty()){
            throw new IllegalArgumentException("해당 폴더가 존재하지 않습니다.");
        }

        BookMark newBookMark = BookMark.of(findMember, findPost.get(), findBookMarkFolder.get());
        bookMarkRepository.save(newBookMark);

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "즐겨찾기 등록 완료"));
    }

    // 즐겨찾기 취소
    @Transactional
    public ResponseEntity<MessageResponseDto> delBookMark(Long folderId, Long postId, Member member){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버가 없습니다.")
        );

        Optional<BookMarkFolder> findBookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, findMember);
        if(findBookMarkFolder.isEmpty()){
            throw new IllegalArgumentException("해당 폴더가 존재하지 않습니다.");
        }

        Optional<BookMark> bookMark = bookMarkRepository.findByBookMarkFolderIdAndPostId(folderId, postId);

        bookMarkRepository.delete(bookMark.get());

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "즐겨찾기 등록 취소"));
    }
}
