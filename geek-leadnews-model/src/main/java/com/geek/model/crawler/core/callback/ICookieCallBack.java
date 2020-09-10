package com.geek.model.crawler.core.callback;

import com.geek.model.crawler.core.cookie.CrawlerCookie;

public interface ICookieCallBack {

    /**
     * 获取 CookieMap。
     *
     * @param url
     * @return
     */
    CrawlerCookie getCookieEntity(String url);

}
