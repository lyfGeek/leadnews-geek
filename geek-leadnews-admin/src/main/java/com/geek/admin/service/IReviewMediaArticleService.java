package com.geek.admin.service;

public interface IReviewMediaArticleService {

    /**
     * 自媒体端发布文章自动审核。
     *
     * @param newsId
     */
    void autoReviewArticleByMedia(Integer newsId);

}
