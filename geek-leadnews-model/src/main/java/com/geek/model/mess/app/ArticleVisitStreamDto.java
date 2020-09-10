package com.geek.model.mess.app;

import lombok.Data;

@Data
public class ArticleVisitStreamDto {

    private Integer articleId;
    private long view;
    private long collect;
    private long comment;
    private long like;

}
