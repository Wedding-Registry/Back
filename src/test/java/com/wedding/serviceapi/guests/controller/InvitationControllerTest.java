package com.wedding.serviceapi.guests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.boards.domain.HusbandAndWifeEachInfo;
import com.wedding.serviceapi.boards.dto.weddinghall.WeddingHallInfoDto;
import com.wedding.serviceapi.boards.service.WeddingHallService;
import com.wedding.serviceapi.exception.NoBoardsIdCookieExistException;
import com.wedding.serviceapi.gallery.domain.GalleryImg;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.service.GalleryService;
import com.wedding.serviceapi.goods.domain.Goods;
import com.wedding.serviceapi.goods.domain.UsersGoods;
import com.wedding.serviceapi.goods.dto.UsersGoodsInfoDto;
import com.wedding.serviceapi.goods.service.UsersGoodsService;
import com.wedding.serviceapi.guests.domain.AttendanceType;
import com.wedding.serviceapi.guests.repository.GuestsRepository;
import com.wedding.serviceapi.guests.service.InvitationService;
import com.wedding.serviceapi.guests.vo.RequestAttendanceVo;
import com.wedding.serviceapi.users.domain.LoginType;
import com.wedding.serviceapi.users.domain.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import javax.servlet.http.HttpServletResponse;

import java.util.List;

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
    @MockBean
    UsersGoodsService usersGoodsService;
    @MockBean
    WeddingHallService weddingHallService;
    @MockBean
    GuestsRepository guestsRepository;

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

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("status").value(400))
                .andExpect(jsonPath("message").value("어떤 게시판인지 알 수 없습니다. 게시판 정보를 보내주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("등록된 사진 요청 성공")
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

    @Test
    @DisplayName("등록된 상품 목록 요청 성공")
    @WithCustomMockUser
    void findUsersGoods() throws Exception {
        // given
        String url = "/invitation/weddingHall/products";
        Goods goods1 = Goods.builder().goodsImgUrl("goods1").build();
        Goods goods2 = Goods.builder().goodsImgUrl("goods2").build();
        UsersGoods usersGoods1 = UsersGoods.builder().id(1L).goods(goods1).updatedUsersGoodsName("goods1").updatedUsersGoodsPrice(10000).usersGoodsTotalDonation(3000).build();
        UsersGoods usersGoods2 = UsersGoods.builder().id(2L).goods(goods2).updatedUsersGoodsName("goods2").updatedUsersGoodsPrice(30000).usersGoodsTotalDonation(5000).build();
        UsersGoodsInfoDto data1 = new UsersGoodsInfoDto(usersGoods1);
        UsersGoodsInfoDto data2 = new UsersGoodsInfoDto(usersGoods2);
        doAnswer(invocation -> {
            HttpServletResponse res = invocation.getArgument(1);
            res.addCookie(new Cookie("isRegistered", "true"));
            return List.of(data1, data2);
        }).when(invitationService).findAllUsersGoods(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class), anyLong());
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

    @Test
    @DisplayName("등록된 결혼식장 정보 요청 성공")
    @WithCustomMockUser
    void findWeddingHallInfo() throws Exception {
        // given
        String url = "/invitation/weddingHall/info";
        Users user = Users.builder().name("test").loginType(LoginType.SERVICE).build();
        Boards boards = Boards.builder().users(user).uuidFirst("first").uuidSecond("second").husband(new HusbandAndWifeEachInfo("husband", "신한은행", "110111111"))
                .wife(new HusbandAndWifeEachInfo("wife", "국민은행", "110211212"))
                .address("강남").date("2023-06-17").time("15:30").build();
        WeddingHallInfoDto data = new WeddingHallInfoDto(boards);
        doAnswer(invocation -> {
            HttpServletResponse res = invocation.getArgument(1);
            res.addCookie(new Cookie("isRegistered", "true"));
            return data;
        }).when(invitationService).findWeddingHallInfo(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class), anyLong());
        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(200))
                .andExpect(jsonPath("data.users.size()").value(2))
                .andExpect(jsonPath("data.account.size()").value(2))
                .andExpect(jsonPath("data.location").value("강남"))
                .andExpect(jsonPath("data.weddingDate").value("2023-06-17"))
                .andExpect(jsonPath("data.weddingTime").value("15:30"))
                .andExpect(cookie().exists("isRegistered"))
                .andExpect(cookie().value("isRegistered", "true"))
                .andDo(print());
    }

    @Test
    @DisplayName("초대된 사용자가 참석 여부를 결정해서 전달한다.")
    @WithCustomMockUser
    void checkGuestAttendance() throws Exception {
        // given
        String url = "/invitation/weddingHall/attendance";
        Users user = Users.builder().name("test").loginType(LoginType.SERVICE).build();
        Boards boards = Boards.builder().users(user).uuidFirst("first").uuidSecond("second").husband(new HusbandAndWifeEachInfo("husband", "신한은행", "110111111"))
                .wife(new HusbandAndWifeEachInfo("wife", "국민은행", "110211212"))
                .address("강남").date("2023-06-17").time("15:30").build();
        WeddingHallInfoDto data = new WeddingHallInfoDto(boards);
        doAnswer(invocation -> {
            HttpServletResponse res = invocation.getArgument(1);
            res.addCookie(new Cookie("isRegistered", "true"));
            return null;
        }).when(invitationService).checkAttendance(any(MockHttpServletRequest.class), any(MockHttpServletResponse.class), anyLong(), any(AttendanceType.class));

        RequestAttendanceVo body = new RequestAttendanceVo("yes");
        // when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true))
                .andExpect(jsonPath("status").value(202))
                .andExpect(jsonPath("data").isEmpty())
                .andExpect(cookie().exists("isRegistered"))
                .andExpect(cookie().value("isRegistered", "true"))
                .andDo(print());
    }
}