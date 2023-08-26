package com.wedding.serviceapi.util.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RegisterUsersGoodsCrawler implements CrawlingUtil {

    @Override
    public Document crawlWebPage(String url) {
        Document document;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new IllegalArgumentException("해당하는 상품이 없습니다.");
        }
        return document;
    }

    @Override
    public String getProductName(Document document) {
        String text = document.select("._10hph879os").select("._22kNQuEXmb").text();
        return text.equals("") ? null : text;
    }

    @Override
    public Integer getProductCurrentPrice(Document document) {
        Integer money = Integer.MAX_VALUE;
        Elements elements = document.select("span._1LY7DqCnwR");
        for (Element element : elements) {
            String extractedString = element.text();
            Integer extractedMoney = Integer.valueOf(extractedString.replaceAll(",", ""));
            money = Math.min(money, extractedMoney);
        }
        return money == Integer.MAX_VALUE ? null : money;
    }

    @Override
    public String getProductImgUrl(Document document) {
        String imageUrl = document.select("div._2tT_gkmAOr").select("img").attr("src");

        if (imageUrl.equals("")) {
            imageUrl = document.select("div.bd_1uFKu").select("div.bd_2PG3r").select("img").attr("src");
        }

        return imageUrl.equals("") ? null : imageUrl;
    }

}
