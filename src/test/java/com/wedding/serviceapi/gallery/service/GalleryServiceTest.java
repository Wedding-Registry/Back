package com.wedding.serviceapi.gallery.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.gallery.domain.GalleryImg;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.repository.GalleryImgRepository;
import com.wedding.serviceapi.gallery.repository.S3Repository;
import com.wedding.serviceapi.users.domain.Users;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class GalleryServiceTest {

    @InjectMocks
    GalleryService galleryService;

    @Mock
    GalleryImgRepository galleryImgRepository;
    @Mock
    BoardsRepository boardsRepository;
    @Mock
    S3Repository s3Repository;

    MockMultipartFile multipartFile = new MockMultipartFile("mock file", (byte[]) null);

    private Long boardsId;
    private Long usersId;
    private Boards boards;
    private Users users;
    private GalleryImg galleryImg;

    @BeforeEach
    void init() {
        boardsId = 1L;
        usersId = 1L;

        users = Users.builder().id(usersId).name("user").build();
        boards = Boards.builder().id(boardsId).uuidFirst("uuid1").uuidSecond("uuid2").users(users).build();
        galleryImg = new GalleryImg(boards, "testUrl");
    }

    @Test
    @DisplayName("해당 결혼 게시판이 없는경우")
    void noSuchWeddingBoard() {
        // given
        MultipartFile multipartFile = null;
        Mockito.doReturn(Optional.empty()).when(boardsRepository).findByIdAndUsersIdNotDeleted(boardsId, usersId);

        // then
        assertThatThrownBy(() -> galleryService.uploadGalleryImg(usersId, boardsId, multipartFile))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미지 저장 완료")
    void successUploadGalleryImg() {
        // given
        String galleryImgUrl = "testUrl";
        Mockito.doReturn(Optional.of(boards)).when(boardsRepository).findByIdAndUsersIdNotDeleted(boardsId, usersId);
        Mockito.doReturn(galleryImgUrl).when(s3Repository).uploadObject(multipartFile, usersId);
        GalleryImg savedGalleryImg = new GalleryImg(1L, boards, galleryImgUrl);
        Mockito.doReturn(savedGalleryImg).when(galleryImgRepository).save(Mockito.any(GalleryImg.class));
        // when
        S3ImgInfoDto s3ImgInfoDto = galleryService.uploadGalleryImg(usersId, boardsId, multipartFile);

        // then
        assertThat(s3ImgInfoDto.getGalleryImgId()).isEqualTo(1L);
        assertThat(s3ImgInfoDto.getGalleryImgUrl()).isEqualTo("testUrl");
    }

    @Test
    @DisplayName("모든 갤러리 사진 찾기")
    void findAllGalleryImg() {
        // given
        GalleryImg galleryImg1 = new GalleryImg(boards, "url1");
        GalleryImg galleryImg2 = new GalleryImg(boards, "url2");
        GalleryImg galleryImg3 = new GalleryImg(boards, "url3");

        ArrayList<GalleryImg> imgList = new ArrayList<>(List.of(new GalleryImg[]{galleryImg1, galleryImg2, galleryImg3}));
        Mockito.doReturn(imgList).when(galleryImgRepository).findAllByBoardsIdAndUsersIdNotDeleted(boardsId, usersId);
        // when
        List<S3ImgInfoDto> allGalleryImg = galleryService.findAllGalleryImg(usersId, boardsId);
        // then
        assertThat(allGalleryImg).hasSize(3);
    }

    @Test
    @DisplayName("갤러리 이미지 삭제 실패")
    void deleteGalleryImgFail() {
        // given
        Mockito.doReturn(Optional.empty()).when(galleryImgRepository).findByIdAndBoardsIdAndUsersIdNotDeleted(1L, boardsId, usersId);
        // then
        assertThatThrownBy(() -> galleryService.deleteGalleryImg(1L, usersId, boardsId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}























