package com.geek.model.mess.admin;

import com.geek.model.article.pojos.ApArticleConfig;
import com.geek.model.article.pojos.ApArticleContent;
import com.geek.model.article.pojos.ApAuthor;
import lombok.Data;

@Data
public class AutoReviewClNewsSuccess {

    private ApArticleConfig apArticleConfig;
    private ApArticleContent apArticleContent;
    private ApAuthor apAuthor;

}
