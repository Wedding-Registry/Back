package com.wedding.serviceapi.gallery.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AmazonS3Repository implements S3Repository {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.url}")
    private String s3Url;
    private final String BUCKET_NAME = "wedding-registry-dev/gallery";

    @Override
    public String uploadObject(MultipartFile file, Long usersId) {
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
        amazonS3.putObject(BUCKET_NAME, key, inputStream, objectMetadata);
        return galleryImgUrl;
    }

    @Override
    public void deleteObject(String galleryImgUrl) {
        String[] splitUrl = galleryImgUrl.split("/");
        String key = splitUrl[splitUrl.length - 1];
        amazonS3.deleteObject(BUCKET_NAME, key);
    }
}
