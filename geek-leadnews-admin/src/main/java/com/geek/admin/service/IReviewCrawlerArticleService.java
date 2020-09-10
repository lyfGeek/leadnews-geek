package com.geek.admin.service;

import com.geek.model.crawler.pojos.ClNews;

public interface IReviewCrawlerArticleService {

    /**
     * 爬虫端发布文章审核。
     *
     * @throws Exception
     */
    void autoReviewArticleByCrawler(ClNews clNews) throws Exception;

    void autoReviewArticleByCrawler() throws Exception;

    void autoReviewArticleByCrawler(Integer clNewsId) throws Exception;

}
