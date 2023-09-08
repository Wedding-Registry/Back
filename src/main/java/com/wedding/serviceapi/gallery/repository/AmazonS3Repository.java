package com.wedding.serviceapi.gallery.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.wedding.serviceapi.exception.S3ObjectException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AmazonS3Repository implements S3Repository {

    private final AmazonS3 amazonS3;
    private final List<String> s3ImgUrlsNeedDelete = new ArrayList<>();

    @Value("${cloud.aws.s3.url}")
    private String s3Url;
    @Value("${custom.s3.bucket-name}")
    private String BUCKET_NAME;

    @Override
    public String uploadObject(MultipartFile file, Long usersId) {
        // s3 객체에 저장하기
        // 파일 이름 변환 -> s3 key 값으로 userId-uuid 로 진행
        String galleryImgUrl;
        try (InputStream inputStream = file.getInputStream()) {
            String key = usersId + "-" + UUID.randomUUID();
            galleryImgUrl = s3Url + key;

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());
            log.info("objectMetadata = {}", objectMetadata);
            amazonS3.putObject(BUCKET_NAME, key, inputStream, objectMetadata);
        } catch (Exception e) {
            throw new S3ObjectException("잘못된 파일입니다.", e);
        }

        return galleryImgUrl;
    }

    @Override
    public void deleteObject(String galleryImgUrl) {
        String[] splitUrl = galleryImgUrl.split("/");
        String key = splitUrl[splitUrl.length - 1];
        try {
            amazonS3.deleteObject(BUCKET_NAME, key);
        } catch (Exception e) {
            s3ImgUrlsNeedDelete.add(key);
            throw new S3ObjectException("이미지 삭제 중 문제가 발생했습니다.", e);
        }
    }

    @Scheduled(initialDelay = 0, fixedDelay = 1000 * 60 * 60 * 24)
    public void deleteNotUsingS3Img() {
        log.info("deleteNotUsingS3Img batch start at = {}", LocalDateTime.now());
        log.info("s3ImgUrlsNeedDelete = {}", s3ImgUrlsNeedDelete);
        s3ImgUrlsNeedDelete.forEach(key -> amazonS3.deleteObject(BUCKET_NAME, key));
        s3ImgUrlsNeedDelete.clear();
    }
}
