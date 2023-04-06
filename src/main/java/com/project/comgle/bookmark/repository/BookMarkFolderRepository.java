package com.project.comgle.bookmark.repository;

import com.project.comgle.bookmark.entity.BookMarkFolder;
import com.project.comgle.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookMarkFolderRepository extends JpaRepository<BookMarkFolder,Long> {

    List<BookMarkFolder> findAllByMember(Member member);

    Optional<BookMarkFolder> findByBookMarkFolderNameAndMember(String folderName, Member member);

    Optional<BookMarkFolder> findByIdAndMember(Long folderId, Member member);

    Long countAllByMember(Member member);

}