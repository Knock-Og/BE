package com.project.comgle.service;

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

    // 즐겨찾기 폴더 생성
    @Transactional
    public ResponseEntity<MessageResponseDto> createBookMarkFolder(String folderName, Member member){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버가 없습니다.")
        );

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
    public ResponseEntity<MessageResponseDto> delBookMarkFolder(String folderName, Member member){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버가 없습니다.")
        );

        Optional<BookMarkFolder> bookMarkFolder = bookMarkFolderRepository.findByBookMarkFolderNameAndMember(folderName, member);
        bookMarkFolderRepository.delete(bookMarkFolder.get());

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "즐겨찾기 폴더 삭제"));
    }

    // 즐겨찾기 폴더(만) 조회
    @Transactional(readOnly = true)
    public List<String> readBookMarkFolder(Member member){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버가 없습니다.")
        );

        List<BookMarkFolder> bookMarkFolders = bookMarkFolderRepository.findAllByMember(findMember);
        List<String> bookMarkFoldersList = new ArrayList<>();

        for (int i = 0; i < bookMarkFolders.size(); i++) {
            bookMarkFoldersList.add(bookMarkFolders.get(i).getBookMarkFolderName());
        }

        return bookMarkFoldersList;
    }

    // 즐겨찾기 추가
    @Transactional
    public ResponseEntity<MessageResponseDto> postBookMark(Long postId, String folderName, Member member){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버가 없습니다.")
        );

        Optional<Post> findPost = postRepository.findById(postId);
        Optional<BookMarkFolder> findBookMarkFolder = bookMarkFolderRepository.findByBookMarkFolderNameAndMember(folderName, findMember);

        BookMark newBookMark = BookMark.of(findMember, findPost.get(), findBookMarkFolder.get());
        bookMarkRepository.save(newBookMark);

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "즐겨찾기 등록 완료"));
    }

    // 즐겨찾기 취소
    @Transactional
    public ResponseEntity<MessageResponseDto> delBookMark(Long postId, String folderName, Member member){
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new IllegalArgumentException("해당 멤버가 없습니다.")
        );

        Optional<BookMarkFolder> findBookMarkFolder = bookMarkFolderRepository.findByBookMarkFolderNameAndMember(folderName, findMember);
        Optional<BookMark> bookMark = bookMarkRepository.findByBookMarkFolderIdAndPostId(findBookMarkFolder.get().getId(), postId);

        bookMarkRepository.delete(bookMark.get());

        return ResponseEntity.ok().body(MessageResponseDto.of(HttpStatus.OK.value(), "즐겨찾기 등록 취소"));
    }
}
