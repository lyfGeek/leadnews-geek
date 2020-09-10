package com.geek.model.article.pojos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class ApArticleContent {

    private Integer id;
    // 增加注解，JSON 序列化时自动混淆加密。
    @IdEncrypt
    private Integer articleId;
    private String content;

}
