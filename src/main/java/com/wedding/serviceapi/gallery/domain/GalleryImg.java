package com.wedding.serviceapi.gallery.domain;

import com.wedding.serviceapi.boards.domain.Boards;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class GalleryImg {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gallery_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boards_id")
    private Boards boards;

    private String galleryImgUrl;

    public GalleryImg(Boards boards, String galleryImgUrl) {
        this.boards = boards;
        this.galleryImgUrl = galleryImgUrl;
    }
}
