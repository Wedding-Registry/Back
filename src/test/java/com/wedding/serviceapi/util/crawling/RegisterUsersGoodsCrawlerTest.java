package com.wedding.serviceapi.util.crawling;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.AbstractThrowableAssert;
import org.assertj.core.api.Assertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RegisterUsersGoodsCrawlerTest {

    private final RegisterUsersGoodsCrawler crawler = new RegisterUsersGoodsCrawler();

    String url;
    File file;

    @BeforeEach
    void init() {
        url = "https://shopping.naver.com/logistics/products/5533808584?NaPm=ct%3Dlgwhx75j%7Cci%3Dshoppingvertical%7Ctr%3Davgts%7Chk%3D0cb27be3bf4989cdc7da49962c1b31a7f31653d2";
        String url = System.getProperty("user.dir");
        String path = url + "/src/test/java/com/wedding/serviceapi/util/crawling/test.html";
        file = new File(path);
    }

    @Test
    @DisplayName("잘못된 주소여서 html document를 가져오지 못함")
    void notValidUrl() {
        // given
        String wrongUrl = null;
        // when
        assertThatThrownBy(() -> crawler.crawlWebPage(wrongUrl))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주소를 통해 html document 가져오기 성공")
    void successCrawlingWebPage() {
        // when
        Document document = crawler.crawlWebPage(url);
        // then
        log.info(document.toString());
        assertThat(document).isNotNull();
    }

    @Test
    @DisplayName("상품 이름 가져오기")
    void getProductName() throws IOException {
        // given
        Document document = Jsoup.parse(file, "utf-8", "http://localhost");
        // when
        String name = crawler.getProductName(document);
        // then
        assertThat(name).isEqualTo("[도착보장] 신세계푸드 대용량 생지 모음");
    }

    @Test
    @DisplayName("상품 이름이 null이 나오는 경우")
    void nullProductName() {
        // given
        Document document = new Document("test");
        // when
        String productName = crawler.getProductName(document);
        // then
        assertThat(productName).isNull();
    }

    @Test
    @DisplayName("현재 상품 가격이 null이 나오는 경우")
    void nullPrice() {
        // given
        Document document = new Document("test");
        // when
        Integer productCurrentPrice = crawler.getProductCurrentPrice(document);
        // then
        log.info("price = {}", productCurrentPrice);
        assertThat(productCurrentPrice).isNull();
    }

    @Test
    @DisplayName("현재 상품가격 가져오기(ex 할인가)")
    void getCurrentPrice() throws IOException {
        // given
        Document document = Jsoup.parse(file, "utf-8", "http://localhost");
        // when
        Integer price = crawler.getProductCurrentPrice(document);
        // then
        assertThat(price).isEqualTo(19900);
    }

    @Test
    @DisplayName("상품 이미지가 null이 나오는 경우")
    void nullImageUrl() {
        // given
        Document document = new Document("test");
        // when
        String imgUrl = crawler.getProductImgUrl(document);
        // then
        assertThat(imgUrl).isNull();
    }

    @Test
    @DisplayName("상품 이미지 등록 성공")
    void getProductImgUrl() throws IOException {
        // given
        String answer = "https://shop-phinf.pstatic.net/20220513_253/1652416123748aQzPM_JPEG/53552022442542369_1254502928.jpg?type=m510";
        Document document = Jsoup.parse(file, "utf-8", "http://localhost");
        // when
        String productImgUrl = crawler.getProductImgUrl(document);
        // then
        assertThat(productImgUrl).isEqualTo(answer);
    }
}






























