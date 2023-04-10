package com.project.comgle.bookmark.repository;

import com.project.comgle.bookmark.entity.BookMark;
import com.project.comgle.member.entity.Member;
import com.project.comgle.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    @Query("SELECT f.id from BookMarkFolder f join BookMark b on f.id = b.bookMarkFolder.id where f.member = :member and b.post = :post")
    List<Integer> findFoldersByPost(@Param("member") Member member, @Param("post") Post post);

    Optional<BookMark> findByBookMarkFolderIdAndPostId(Long folderId, Long postId);

    Page<BookMark> findAllByBookMarkFolderId(Long folderId, Pageable pageable);

    void deleteAllByPost(Post post);

    int countByBookMarkFolderId(Long folderId);

    void deleteAllByPostIn(List<Post> post);

}
