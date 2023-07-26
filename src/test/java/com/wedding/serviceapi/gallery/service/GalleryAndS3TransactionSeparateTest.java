package com.wedding.serviceapi.gallery.service;

import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.gallery.domain.GalleryImg;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.repository.GalleryImgRepository;
import com.wedding.serviceapi.gallery.repository.S3Repository;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import com.wedding.serviceapi.users.repository.UsersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GalleryAndS3TransactionSeparateTest {

    @MockBean
    S3Repository s3Repository;

    @Autowired
    GalleryService galleryService;
    @Autowired
    GalleryImgRepository galleryImgRepository;
    @Autowired
    BoardsRepository boardsRepository;
    @Autowired
    UsersRepository usersRepository;

    @AfterEach
    void tearDown() {
        galleryImgRepository.deleteAllInBatch();
        boardsRepository.deleteAllInBatch();
        usersRepository.deleteAllInBatch();
    }

    @DisplayName("갤러리 이미지 생성 시 트랜잭션 시작과 끝 확인")
    @Test
    void checkTransaction() {
        // given
        Users user = Users.builder()
                .name("user")
                .loginType(LoginType.SERVICE)
                .password("password")
                .build();
        Boards board = Boards.builder()
                .uuidFirst("uuid1")
                .uuidSecond("uuid2")
                .users(user)
                .build();

        Users savedUser = usersRepository.saveAndFlush(user);
        Boards savedBoard = boardsRepository.saveAndFlush(board);

        String galleryImgUrl = "testUrl";
        MultipartFile file = new MockMultipartFile("file", (byte[]) null);
        doReturn(galleryImgUrl).when(s3Repository).uploadObject(file, savedUser.getId());

        // when
        S3ImgInfoDto s3ImgInfoDto = galleryService.uploadGalleryImg(savedUser.getId(), savedBoard.getId(), file);
        // then
        Assertions.assertThat(s3ImgInfoDto.getGalleryImgUrl()).isEqualTo("testUrl");
    }

    @DisplayName("갤러리 이미지 삭제 시 트랜잭션 확인")
    @Test
    void checkTransactionWhenDeleteImage() {
        // given
        Users user = Users.builder()
                .name("user")
                .loginType(LoginType.SERVICE)
                .password("password")
                .build();
        Boards board = Boards.builder()
                .uuidFirst("uuid1")
                .uuidSecond("uuid2")
                .users(user)
                .build();
        GalleryImg galleryImg = GalleryImg.builder()
                .galleryImgUrl("testUrl")
                .boards(board)
                .build();
        usersRepository.save(user);
        boardsRepository.save(board);
        GalleryImg savedGalleryImg = galleryImgRepository.save(galleryImg);
        doNothing().when(s3Repository).deleteObject(savedGalleryImg.getGalleryImgUrl());
        // when
        // then
        galleryService.deleteGalleryImg(savedGalleryImg.getId());
    }

    @DisplayName("없는 이미지를 삭제하려는 경우 해당하는 이미지가 없다는 에러를 발생시킨다.")
    @Test
    void NoSuchElementException() {
        // given
        // when
        // then
        Assertions.assertThatThrownBy(() -> galleryService.deleteGalleryImg(2L))
                .isInstanceOf(NoSuchElementException.class);
    }
}