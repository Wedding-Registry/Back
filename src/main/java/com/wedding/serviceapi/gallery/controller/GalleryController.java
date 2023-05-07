package com.wedding.serviceapi.gallery.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.dto.UploadS3ImgDto;
import com.wedding.serviceapi.gallery.service.GalleryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/gallery/img")
@RequiredArgsConstructor
public class GalleryController {

    private final GalleryService galleryService;

    @PostMapping
    public ResponseVo<UploadS3ImgDto> uploadGalleryImg(@LoginUser LoginUserVo loginUserVo,
                                                       @RequestParam("galleryImg") MultipartFile file) throws IOException {

        log.info("[uploadGalleryImg Controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        UploadS3ImgDto data = galleryService.uploadGalleryImg(loginUserVo.getUserId(), loginUserVo.getBoardsId(), file);

        return new ResponseVo<>(true, HttpStatus.CREATED.value(), data);
    }

    @GetMapping
    public ResponseVo<List<S3ImgInfoDto>> findAllGalleryImg(@LoginUser LoginUserVo loginUserVo) {
        log.info("[findAllGalleryImg Controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        List<S3ImgInfoDto> data = galleryService.findAllGalleryImg(loginUserVo.getUserId(), loginUserVo.getBoardsId());
        return new ResponseVo<>(true, HttpStatus.OK.value(), data);
    }

    @DeleteMapping
    public ResponseVo<Void> deleteGalleryImg(@LoginUser LoginUserVo loginUserVo, @RequestParam Long galleryImgId) {
        log.info("[deleteGalleryImg Controller] usersId = {}, boardsid = {}, galleryImgId = {}",
                loginUserVo.getUserId(), loginUserVo.getBoardsId(), galleryImgId);

        galleryService.deleteGalleryImg(galleryImgId, loginUserVo.getUserId(), loginUserVo.getBoardsId());
        return new ResponseVo<>(true, HttpStatus.ACCEPTED.value(), null);
    }
}
