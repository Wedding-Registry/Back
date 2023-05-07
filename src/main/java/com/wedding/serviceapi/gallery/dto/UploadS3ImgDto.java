package com.wedding.serviceapi.gallery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadS3ImgDto {
    private Long galleryImgId;
    private String galleryImgUrl;
}
