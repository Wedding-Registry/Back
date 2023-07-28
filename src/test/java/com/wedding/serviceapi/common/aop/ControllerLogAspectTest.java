package com.wedding.serviceapi.common.aop;

import com.wedding.serviceapi.auth.vo.LoginUserVo;
import com.wedding.serviceapi.gallery.controller.GalleryController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class ControllerLogAspectTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    Method findGalleryImgMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        findGalleryImgMethod = GalleryController.class.getMethod("findAllGalleryImg", LoginUserVo.class);
    }

    @Test
    @DisplayName("어노테이션 기반 pointcut이 해당 메서드를 찾는지 확인합니다.")
    void annotation() {
        // given
        // when
        pointcut.setExpression("@annotation(org.springframework.web.bind.annotation.GetMapping)");
        // then
        Assertions.assertThat(pointcut.matches(findGalleryImgMethod, GalleryController.class)).isTrue();

    }
}