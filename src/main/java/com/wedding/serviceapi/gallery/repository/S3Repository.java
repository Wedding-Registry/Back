package com.wedding.serviceapi.gallery.repository;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface S3Repository {

    String uploadObject(MultipartFile file, Long usersId);

    void deleteObject(String galleryImgUrl);
}
