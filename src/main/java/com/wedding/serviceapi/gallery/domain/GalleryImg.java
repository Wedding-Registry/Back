package com.wedding.serviceapi.gallery.domain;

import com.wedding.serviceapi.boards.domain.Boards;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class GalleryImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gallery_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boards_id")
    private Boards boards;

    private String galleryImgUrl;
}