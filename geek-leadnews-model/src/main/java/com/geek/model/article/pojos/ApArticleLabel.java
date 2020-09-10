package com.geek.model.article.pojos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ApArticleLabel {

    private Integer id;
    private Integer articleId;
    private Integer labelId;
    private Integer count;

    public ApArticleLabel(Integer articleId, Integer labelId) {
        this.articleId = articleId;
        this.labelId = labelId;
    }

}
