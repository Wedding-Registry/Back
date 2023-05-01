package com.wedding.serviceapi.util.crawling;

import org.jsoup.nodes.Document;

public interface CrawlingUtil {

    Document crawlWebPage(String url);

    String getProductName(Document document);

    Integer getProductCurrentPrice(Document document);

    String getProductImgUrl(Document document);
}
