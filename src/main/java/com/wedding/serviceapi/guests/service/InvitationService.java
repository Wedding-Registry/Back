package com.wedding.serviceapi.guests.service;

import com.wedding.serviceapi.guests.invitationinfo.InvitationInfoSetter;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.service.GalleryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InvitationService {

    private final GalleryService galleryService;
    private final InvitationInfoSetter invitationInfoSetter;

    public List<S3ImgInfoDto> findAllGalleryImg(HttpServletRequest request, HttpServletResponse response, Long usersId) {
        invitationInfoSetter.checkInvitationInfoAndSettingInfoIfNotExist(request, response, usersId);
        long boardsId = invitationInfoSetter.getBoardsId(request);
        return galleryService.findAllGalleryImg(boardsId);
    }

}
