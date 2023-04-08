package com.project.comgle.bookmark.repository;

import com.project.comgle.bookmark.entity.BookMark;
import com.project.comgle.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    Optional<BookMark> findByBookMarkFolderIdAndPostId(Long folderId, Long postId);

    Page<BookMark> findAllByBookMarkFolderId(Long folderId, Pageable pageable);

    void deleteAllByPost(Post post);

    int countByBookMarkFolderId(Long folderId);

    void deleteAllByPostIn(List<Post> post);

}
