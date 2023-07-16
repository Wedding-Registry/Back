package com.wedding.serviceapi.gallery.dto;

import com.wedding.serviceapi.gallery.domain.GalleryImg;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class S3ImgInfoDto {
    private Long galleryImgId;
    private String galleryImgUrl;

    public S3ImgInfoDto(GalleryImg galleryImg) {
        this.galleryImgId = galleryImg.getId();
        this.galleryImgUrl = galleryImg.getGalleryImgUrl();
    }

    public static S3ImgInfoDto from(GalleryImg galleryImg) {
        return new S3ImgInfoDto(galleryImg);
    }
}
