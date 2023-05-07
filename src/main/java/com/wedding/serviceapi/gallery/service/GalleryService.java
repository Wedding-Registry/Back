package com.wedding.serviceapi.gallery.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.gallery.domain.GalleryImg;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.dto.UploadS3ImgDto;
import com.wedding.serviceapi.gallery.repository.GalleryImgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class GalleryService {

    @Value("${cloud.aws.s3.url}")
    private String s3Url;

    private final String BUCKET_NAME = "wedding-registry-dev/gallery";

    private final GalleryImgRepository galleryImgRepository;
    private final BoardsRepository boardsRepository;
    private final AmazonS3 amazonS3;

    public UploadS3ImgDto uploadGalleryImg(Long usersId, Long boardsId, MultipartFile file) {
        Boards boards = boardsRepository.findByIdAndUsersIdNotDeleted(boardsId, usersId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결혼 게시판이 없습니다."));

        // s3 객체에 저장하기
        // 파일 이름 변환 -> s3 key 값으로 userId-uuid 로 진행
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new IllegalArgumentException("잘못된 파일입니다.");
        }

        String key = usersId + "-" + UUID.randomUUID();
        String galleryImgUrl = s3Url + key;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        PutObjectResult putObjectResult = amazonS3.putObject(BUCKET_NAME, key, inputStream, objectMetadata);
        String eTag = putObjectResult.getETag();

        GalleryImg galleryImg = new GalleryImg(boards, galleryImgUrl);
        GalleryImg savedGalleryImg = galleryImgRepository.save(galleryImg);
        return new UploadS3ImgDto(savedGalleryImg.getId(), savedGalleryImg.getGalleryImgUrl());
    }

    public List<S3ImgInfoDto> findAllGalleryImg(Long usersId, Long boardsId) {
        List<GalleryImg> galleryImgList = galleryImgRepository.findAllByBoardsIdAndUsersIdNotDeleted(boardsId, usersId);

        return galleryImgList.stream().map(S3ImgInfoDto::new).collect(Collectors.toList());
    }

    public void deleteGalleryImg(Long galleryImgId, Long usersId, Long boardsId) {
        GalleryImg galleryImg = galleryImgRepository.findByIdAndBoardsIdAndUsersId(galleryImgId, boardsId, usersId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 사진이 없습니다."));

        String galleryImgUrl = galleryImg.getGalleryImgUrl();
        String[] splitUrl = galleryImgUrl.split("/");
        String key = splitUrl[splitUrl.length - 1];
        amazonS3.deleteObject(BUCKET_NAME, key);

        galleryImgRepository.delete(galleryImg);
    }

}






















