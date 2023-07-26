package com.wedding.serviceapi.gallery.service;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DecoratedDataSource;
import com.wedding.serviceapi.exception.NotDeleteGalleryImgException;
import com.wedding.serviceapi.gallery.domain.GalleryImg;
import com.wedding.serviceapi.gallery.repository.GalleryImgRepository;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GalleryImgRepositoryFacade {

    private final DecoratedDataSource dataSource;
    private final GalleryImgRepository galleryImgRepository;

    @Transactional
    public GalleryImg save(GalleryImg galleryImg) {
        printConnectionStatus();
        try {
            return galleryImgRepository.save(galleryImg);
        } catch (Exception e) {
            throw new RuntimeException("갤러리 이미지 저장 중 에러가 발생했습니다.", e);
        }
    }

    @Transactional
    public String deleteById(Long galleryImgId) {
        printConnectionStatus();
        try {
            GalleryImg galleryImg = galleryImgRepository.findById(galleryImgId)
                    .orElseThrow(() -> new NoSuchElementException("해당하는 사진이 없습니다."));
            galleryImgRepository.delete(galleryImg);
            return galleryImg.getGalleryImgUrl();
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new NotDeleteGalleryImgException("갤러리 이미지 삭제 중 에러가 발생했습니다", e);
        }
    }

    private void printConnectionStatus() {
        HikariDataSource hikariDataSource = (HikariDataSource) dataSource.getRealDataSource();
        HikariPoolMXBean hikariPoolMXBean = hikariDataSource.getHikariPoolMXBean();

        log.debug("################################");
        log.debug("현재 active인 connection의 수 : = {}",hikariPoolMXBean.getActiveConnections());
        log.debug("현재 idle인 connection의 수 : {}", hikariPoolMXBean.getIdleConnections());
        log.debug("################################");
    }
}
