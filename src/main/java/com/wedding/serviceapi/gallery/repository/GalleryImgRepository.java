package com.wedding.serviceapi.gallery.repository;

import com.wedding.serviceapi.gallery.domain.GalleryImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GalleryImgRepository extends JpaRepository<GalleryImg, Long> {

    @Query("select g from GalleryImg g where g.boards.id = :boardsId and g.boards.users.id = :usersId and g.boards.deletedAt = false")
    List<GalleryImg> findAllByBoardsIdAndUsersIdNotDeleted(@Param("boardsId") Long boardsId, @Param("usersId") Long usersId);

    @Query("select g from GalleryImg g where g.id = :galleryImgId and g.boards.id = :boardsId and g.boards.users.id = :usersId and g.boards.deletedAt = false")
    Optional<GalleryImg> findByIdAndBoardsIdAndUsersIdNotDeleted(@Param("galleryImgId") Long galleryImgId,
                                                       @Param("boardsId") Long boardsId,
                                                       @Param("usersId") Long usersId);

}
