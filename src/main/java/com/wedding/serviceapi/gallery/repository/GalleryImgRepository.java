package com.wedding.serviceapi.gallery.repository;

import com.wedding.serviceapi.gallery.domain.GalleryImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryImgRepository extends JpaRepository<GalleryImg, Long> {
}
