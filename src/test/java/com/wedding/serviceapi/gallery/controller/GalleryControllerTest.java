package com.wedding.serviceapi.gallery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wedding.serviceapi.WithCustomMockUser;
import com.wedding.serviceapi.boards.domain.Boards;
import com.wedding.serviceapi.gallery.domain.GalleryImg;
import com.wedding.serviceapi.gallery.dto.S3ImgInfoDto;
import com.wedding.serviceapi.gallery.service.GalleryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(GalleryController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc(addFilters = false)
class GalleryControllerTest {


    @MockBean
    private GalleryService galleryService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    Long userId;
    Long boardsId;

    @BeforeEach
    void init() {
        userId = 1L;
        boardsId = 1L;
    }


    @Test
    @DisplayName("해당하는 결혼 게시판이 없는 경우 갤러리 등록 실패")
    @WithCustomMockUser
    void noBoardsFail() throws Exception {
        // given
        String url = "/gallery/img";
        MockMultipartFile file = new MockMultipartFile("galleryImg", (byte[]) null);
        Mockito.doThrow(new IllegalArgumentException("잘못된 파일입니다.")).when(galleryService).uploadGalleryImg(userId, boardsId, file);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                .file(file)
        );
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("잘못된 파일입니다."));
    }

    @Test
    @DisplayName("갤러리 사진 등록 성공")
    @WithCustomMockUser
    void successUploadGalleryImg() throws Exception {
        // given
        String url = "/gallery/img";
        MockMultipartFile file = new MockMultipartFile("galleryImg", (byte[]) null);
        Boards boards = Boards.builder().build();
        S3ImgInfoDto data = new S3ImgInfoDto(new GalleryImg(boards, "galleryUrl"));
        Mockito.doReturn(data).when(galleryService).uploadGalleryImg(userId, boardsId, file);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart(url)
                .file(file));
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(201))
                .andExpect(MockMvcResultMatchers.jsonPath("data.galleryImgUrl").value("galleryUrl"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("갤러리 이미지 찾기 성공")
    @WithCustomMockUser
    void findAllGalleryImg() throws Exception {
        // given
        String url = "/gallery/img";
        Boards boards = Boards.builder().build();
        S3ImgInfoDto data1 = new S3ImgInfoDto(new GalleryImg(boards, "galleryUrl1"));
        S3ImgInfoDto data2 = new S3ImgInfoDto(new GalleryImg(boards, "galleryUrl2"));
        S3ImgInfoDto data3 = new S3ImgInfoDto(new GalleryImg(boards, "galleryUrl3"));
        ArrayList<S3ImgInfoDto> dataList = new ArrayList<>();
        dataList.add(data1);
        dataList.add(data2);
        dataList.add(data3);
        Mockito.doReturn(dataList).when(galleryService).findAllGalleryImg(userId, boardsId);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON));
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("data.length()").value(3))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("갤러리 이미지 삭제 실패")
    @WithCustomMockUser
    void failDeleteGalleryImg() throws Exception {
        // given
        String url = "/gallery/img";
        Mockito.doThrow(new IllegalArgumentException("해당하는 게시판이 없습니다.")).when(galleryService).deleteGalleryImg(1L, userId, boardsId);

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .param("galleryImgId", "1"));
        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("해당하는 게시판이 없습니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("갤러리 이미지 삭제 성공")
    @WithCustomMockUser
    void deleteGalleryImg() throws Exception {
        // given
        String url = "/gallery/img";
        Mockito.doNothing().when(galleryService).deleteGalleryImg(1L, userId, boardsId);
        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(url)
                .param("galleryImgId", "1"));

        // then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value(202))
                .andExpect(MockMvcResultMatchers.jsonPath("data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }
}






























