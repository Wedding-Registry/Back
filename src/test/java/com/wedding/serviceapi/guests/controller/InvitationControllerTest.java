package com.wedding.serviceapi.guests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.exception.NoBoardsIdCookieExistException;
import com.wedding.serviceapi.gallery.domain.GalleryImg;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.service.GalleryService;
import com.wedding.serviceapi.goods.controller.UsersGoodsController;
import com.wedding.serviceapi.guests.invitationinfo.InvitationInfoSetter;
import com.wedding.serviceapi.guests.service.InvitationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InvitationController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class InvitationControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @SpyBean
    InvitationService invitationService;

    @MockBean
    GalleryService galleryService;
    @MockBean
    InvitationInfoSetter invitationInfoSetter;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("boardsId를 key로 갖는 쿠키가 없는 경우 uuid를 요청하는 응답을 보낸다.")
    @WithCustomMockUser
    void boardsIdCookieNotExist() throws Exception {
        // given
        String url = "/invitation/gallery/images";
        doThrow(new NoBoardsIdCookieExistException("어떤 게시판인지 알 수 없습니다. 게시판 정보를 보내주세요")).when(invitationInfoSetter).checkInvitationInfoAndSettingInfoIfNotExist(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class), anyLong());
        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));


//                .cookie(new Cookie("boardsId", "")));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("어떤 게시판인지 알 수 없습니다. 게시판 정보를 보내주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("등록한 사진 요청 성공")
    @WithCustomMockUser
    void boardsIdCookieExist() throws Exception {
        // given
        String url = "/invitation/gallery/images";

        GalleryImg galleryImg1 = GalleryImg.builder().id(1L).galleryImgUrl("url1").build();
        GalleryImg galleryImg2 = GalleryImg.builder().id(2L).galleryImgUrl("url2").build();
        S3ImgInfoDto s3ImgInfoDto1 = new S3ImgInfoDto(galleryImg1);
        S3ImgInfoDto s3ImgInfoDto2 = new S3ImgInfoDto(galleryImg2);
        doAnswer(invocation -> {
            HttpServletResponse res = invocation.getArgument(1);
            res.addCookie(new Cookie("isRegistered", "true"));
            return List.of(s3ImgInfoDto1, s3ImgInfoDto2);
        }).when(invitationService).findAllGalleryImg(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class), anyLong());
        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data.size()").value(2))
                .andExpect(cookie().exists("isRegistered"))
                .andExpect(cookie().value("isRegistered", "true"))
                .andDo(print());

    }

}