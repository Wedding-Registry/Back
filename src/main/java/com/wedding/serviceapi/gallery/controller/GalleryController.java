package com.wedding.serviceapi.gallery.controller;

import com.wedding.serviceapi.auth.vo.LoginUser;
import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.common.vo.ResponseVo;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.service.GalleryImgRepositoryFacade;
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
    public ResponseVo<S3ImgInfoDto> uploadGalleryImg(@LoginUser LoginUserVo loginUserVo,
                                                       @RequestParam("galleryImg") MultipartFile file) {

        log.info("[uploadGalleryImg Controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        S3ImgInfoDto data = galleryService.uploadGalleryImg(loginUserVo.getUserId(), loginUserVo.getBoardsId(), file);

        return ResponseVo.created(data);
    }

    @GetMapping
    public ResponseVo<List<S3ImgInfoDto>> findAllGalleryImg(@LoginUser LoginUserVo loginUserVo) {
        log.info("[findAllGalleryImg Controller] usersId = {}, boardsId = {}", loginUserVo.getUserId(), loginUserVo.getBoardsId());
        List<S3ImgInfoDto> data = galleryService.findAllGalleryImg(loginUserVo.getUserId(), loginUserVo.getBoardsId());
        return ResponseVo.ok(data);
    }

    @DeleteMapping
    public ResponseVo<Void> deleteGalleryImg(@LoginUser LoginUserVo loginUserVo, @RequestParam Long galleryImgId) {
        log.info("[deleteGalleryImg Controller] usersId = {}, boardsId = {}, galleryImgId = {}",
                loginUserVo.getUserId(), loginUserVo.getBoardsId(), galleryImgId);

        galleryService.deleteGalleryImg(galleryImgId);
        return ResponseVo.accepted();
    }
}
