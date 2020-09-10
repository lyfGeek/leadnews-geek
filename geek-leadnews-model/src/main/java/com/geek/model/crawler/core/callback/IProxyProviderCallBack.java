package com.geek.model.crawler.core.callback;

import com.geek.model.crawler.core.proxy.CrawlerProxy;

import java.util.List;

/**
 * IP 池更新回调。
 */
public interface IProxyProviderCallBack {

    List<CrawlerProxy> getProxyList();

    void unavailable(CrawlerProxy crawlerProxy);

}
