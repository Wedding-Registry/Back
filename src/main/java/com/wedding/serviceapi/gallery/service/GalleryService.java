package com.wedding.serviceapi.gallery.service;

import com.github.gavlyukovskiy.boot.jdbc.decorator.DecoratedDataSource;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.repository.BoardsRepository;
import com.wedding.serviceapi.exception.NotSaveGalleryImgException;
import com.wedding.serviceapi.gallery.domain.GalleryImg;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.repository.GalleryImgRepository;
import com.wedding.serviceapi.gallery.repository.S3Repository;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GalleryService {

    private final GalleryImgRepository galleryImgRepository;
    private final BoardsRepository boardsRepository;
    private final S3Repository s3Repository;
    private final DecoratedDataSource dataSource;
    private final GalleryImgRepositoryFacade galleryImgRepositoryFacade;

    /**
     * 1. S3 객체에 이미지를 먼저 저장한다.
     * 2. 그다음 URL을 DB에 저장한다.
     * 3. S3 객체에 이미지를 저장한 이후 로직에서 에러가 터지면??
     * 4. 캐시에 저장해두고 주기적으로 지워줘야한다
     * 다른 방법은?? -> event나 메시지 / Facade로 런타임 에러를 던져서 롤백시키고 서비스에서 잡아서 s3에 이미지를 바로 지우자.
     *
     * s3를 이용하는 것과 db를 이용하는 것의 트랜잭션을 분리하기 위해 새로운 서비스가 하나 필요함 -> Facade로 정의하는게 맞나??
     */
    public S3ImgInfoDto uploadGalleryImg(Long usersId, Long boardsId, MultipartFile file) {
        Boards boards = boardsRepository.findByIdAndUsersIdNotDeleted(boardsId, usersId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 결혼 게시판이 없습니다."));

        String galleryImgUrl = s3Repository.uploadObject(file, usersId);

        printConnectionStatus();
        GalleryImg galleryImg = new GalleryImg(boards, galleryImgUrl);
        try {
            GalleryImg savedGalleryImg = galleryImgRepositoryFacade.save(galleryImg);
            return S3ImgInfoDto.from(savedGalleryImg);
        } catch (RuntimeException e) {
            // TODO: 2023/07/25 exceptionHandler에서 처리해야함
            s3Repository.deleteObject(galleryImgUrl);
            throw new NotSaveGalleryImgException(e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public List<S3ImgInfoDto> findAllGalleryImg(Long usersId, Long boardsId) {
        List<GalleryImg> galleryImgList = galleryImgRepository.findAllByBoardsIdAndUsersIdNotDeleted(boardsId, usersId);

        return galleryImgList.stream().map(S3ImgInfoDto::from).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<S3ImgInfoDto> findAllGalleryImg(Long boardsId) {
        List<GalleryImg> galleryImgList = galleryImgRepository.findAllByBoardsId(boardsId);

        return galleryImgList.stream().map(S3ImgInfoDto::from).collect(Collectors.toList());
    }

    public void deleteGalleryImg(Long galleryImgId) {
        String deletedGalleryImgUrl = galleryImgRepositoryFacade.deleteById(galleryImgId);
        printConnectionStatus();
        s3Repository.deleteObject(deletedGalleryImgUrl);
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






















