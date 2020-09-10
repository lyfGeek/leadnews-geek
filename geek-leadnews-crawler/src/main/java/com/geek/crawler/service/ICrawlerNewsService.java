package com.geek.crawler.service;

import com.geek.model.crawler.pojos.ClNews;

import java.util.List;

public interface ICrawlerNewsService {

    /**
     * 保存。
     *
     * @param clNews
     */
    void saveNews(ClNews clNews);

    /**
     * 更新。
     *
     * @param clNews
     */
    void updateNews(ClNews clNews);

    /**
     * 删除。
     *
     * @param url
     */
    void deleteByUrl(String url);

    /**
     * 查询。
     *
     * @param clNews
     * @return
     */
    List<ClNews> queryList(ClNews clNews);

}
