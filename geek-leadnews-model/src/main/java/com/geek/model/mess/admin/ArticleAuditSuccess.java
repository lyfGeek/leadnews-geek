package com.geek.model.mess.admin;

import lombok.Data;

@Data
public class ArticleAuditSuccess {

    /**
     * 文章类型。
     */
    private ArticleAuditSuccess.ArticleType type;
    private Integer channelId;
    /**
     * 文章 ID。
     */
    private Integer articleId;

    public enum ArticleType {
        WEMEDIA, CRAWLER
    }

}
