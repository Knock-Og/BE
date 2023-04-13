package com.project.comgle.bookmark.service;

import com.project.comgle.comment.entity.Comment;
import com.project.comgle.comment.repository.CommentRepository;
import com.project.comgle.post.dto.PostPageResponseDto;
import com.project.comgle.bookmark.entity.BookMark;
import com.project.comgle.bookmark.entity.BookMarkFolder;
import com.project.comgle.bookmark.repository.BookMarkFolderRepository;
import com.project.comgle.bookmark.repository.BookMarkRepository;
import com.project.comgle.member.entity.Member;
import com.project.comgle.member.repository.MemberRepository;
import com.project.comgle.post.repository.KeywordRepositoryImpl;
import com.project.comgle.post.repository.PostRepository;
import com.project.comgle.global.common.response.SuccessResponse;
import com.project.comgle.bookmark.dto.BookMarkFolderResponseDto;
import com.project.comgle.post.dto.PostResponseDto;
import com.project.comgle.post.entity.WeightEnum;
import com.project.comgle.global.exception.CustomException;
import com.project.comgle.global.exception.ExceptionEnum;
import com.project.comgle.post.entity.Keyword;
import com.project.comgle.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    private final CommentRepository commentRepository;


    // 즐겨찾기 폴더 추가
    @Transactional
    public SuccessResponse createBookMarkFolder(String folderName, Member member){

        Optional<Member> findMember = memberRepository.findById(member.getId());

        if(findMember.isEmpty() || !findMember.get().isValid()){
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

        return SuccessResponse.of(HttpStatus.CREATED, "Your bookmark folder has been created successfully.");
    }

    // 즐겨찾기 폴더 삭제
    @Transactional
    public SuccessResponse delBookMarkFolder(Long folderId, Member member){

        Optional<Member> findMember = memberRepository.findById(member.getId());

        if(findMember.isEmpty() || !findMember.get().isValid()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        Optional<BookMarkFolder> bookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, member);

        if(bookMarkFolder.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_FOLDER);
        }

        bookMarkFolderRepository.delete(bookMarkFolder.get());

        return SuccessResponse.of(HttpStatus.CREATED, "Your bookmark folder has been deleted successfully.");
    }

    // 즐겨찾기 폴더 수정
    @Transactional
    public SuccessResponse updateBookMarkFolder(Long folderId, String modifyFolderName, Member member){

        Optional<Member> findMember = memberRepository.findById(member.getId());

        if(findMember.isEmpty() || !findMember.get().isValid()){
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

        return SuccessResponse.of(HttpStatus.CREATED, "Your bookmark folder has been modified successfully.");
    }

    // 즐겨찾기 폴더(만) 조회
    @Transactional(readOnly = true)
    public List<BookMarkFolderResponseDto> readBookMarkFolder(Member member){

        Optional<Member> findMember = memberRepository.findById(member.getId());

        if(findMember.isEmpty() || !findMember.get().isValid()){
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

        if(findMember.isEmpty() || !findMember.get().isValid()){
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

        return SuccessResponse.of(HttpStatus.CREATED, "Your post has been added successfully to the folder.");
    }

    // 즐겨찾기 취소
    @Transactional
    public SuccessResponse delBookMark(Long folderId, Long postId, Member member){

        Optional<Member> findMember = memberRepository.findById(member.getId());

        if(findMember.isEmpty() || !findMember.get().isValid()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        Optional<BookMarkFolder> findBookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, member);

        if(findBookMarkFolder.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_FOLDER);
        }

        Optional<BookMark> bookMark = bookMarkRepository.findByBookMarkFolderIdAndPostId(folderId, postId);

        bookMarkRepository.delete(bookMark.get());

        return SuccessResponse.of(HttpStatus.CREATED, "Your post has been deleted successfully to the folder.");
    }

    // 즐겨찾기 폴더 별 게시글 조회
    @Transactional
    public PostPageResponseDto readPostForBookMark(Long folderId, int page, Member member){

        Optional<Member> findMember = memberRepository.findById(member.getId());

        if(findMember.isEmpty() || !findMember.get().isValid()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_MEMBER);
        }

        Optional<BookMarkFolder> findBookMarkFolder = bookMarkFolderRepository.findByIdAndMember(folderId, member);

        if(findBookMarkFolder.isEmpty()){
            throw new CustomException(ExceptionEnum.NOT_EXIST_FOLDER);
        }

        int nowPage = page - 1;   // 현재 페이지
        int size = 10;  // 한 페이지당 게시글 수

        Page<BookMark> bookMarkList = bookMarkRepository.findAllByBookMarkFolderId(findBookMarkFolder.get(), PageRequest.of(nowPage, size));
        int endP = bookMarkList.getTotalPages();

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();
//        List<BookMark> bookMarks = bookMarkRepository.findAllByBookMarkFolderId(folderId);

        for(BookMark b : bookMarkList){

            Post findPost = b.getPost();
            int commentCount = commentRepository.findAllByPost(findPost).size();
            String[] keywordList = findPost.getKeywords().stream().map(Keyword::getKeyword).toArray(String[]::new);
            Integer[] folders = bookMarkRepository.findFoldersByPost(member, findPost).toArray(Integer[]::new);
            int postViews = findPost.getPostViews();

            postResponseDtoList.add(PostResponseDto.of(
                    findPost,
                    findPost.getCategory().getCategoryName(),
                    keywordList,
                    folders,
                    postViews,
                    commentCount));
        }

        return PostPageResponseDto.of(endP,postResponseDtoList);
    }

}
