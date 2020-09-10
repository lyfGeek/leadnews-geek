package com.geek.model.crawler.core.cookie;

import com.geek.model.crawler.core.proxy.CrawlerProxy;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CrawlerHtml {

    private String url;
    private String html;
    private CrawlerProxy proxy;
    private List<CrawlerCookie> crawlerCookieList = null;

    public CrawlerHtml(String url) {
        this.url = url;
    }

}
