package com.wedding.serviceapi.gallery.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.gallery.domain.GalleryImg;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.repository.GalleryImgRepository;
import com.wedding.serviceapi.gallery.repository.S3Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GalleryService {

    private final GalleryImgRepository galleryImgRepository;
    private final BoardsRepository boardsRepository;
    private final S3Repository s3Repository;

    public S3ImgInfoDto uploadGalleryImg(Long usersId, Long boardsId, MultipartFile file) {
        Boards boards = boardsRepository.findByIdAndUsersIdNotDeleted(boardsId, usersId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결혼 게시판이 없습니다."));

        String galleryImgUrl = s3Repository.uploadObject(file, usersId);

        GalleryImg galleryImg = new GalleryImg(boards, galleryImgUrl);
        GalleryImg savedGalleryImg = galleryImgRepository.save(galleryImg);
        return S3ImgInfoDto.from(savedGalleryImg);
    }

    public List<S3ImgInfoDto> findAllGalleryImg(Long usersId, Long boardsId) {
        List<GalleryImg> galleryImgList = galleryImgRepository.findAllByBoardsIdAndUsersIdNotDeleted(boardsId, usersId);

        return galleryImgList.stream().map(S3ImgInfoDto::from).collect(Collectors.toList());
    }

    public List<S3ImgInfoDto> findAllGalleryImg(Long boardsId) {
        List<GalleryImg> galleryImgList = galleryImgRepository.findAllByBoardsId(boardsId);

        return galleryImgList.stream().map(S3ImgInfoDto::from).collect(Collectors.toList());
    }

    public void deleteGalleryImg(Long galleryImgId, Long usersId, Long boardsId) {
        GalleryImg galleryImg = galleryImgRepository.findByIdAndBoardsIdAndUsersIdNotDeleted(galleryImgId, boardsId, usersId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사진이 없습니다."));

        String galleryImgUrl = galleryImg.getGalleryImgUrl();
        s3Repository.deleteObject(galleryImgUrl);
        galleryImgRepository.delete(galleryImg);
    }
}






















