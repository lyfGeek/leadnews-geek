package com.geek.model.article.pojos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class ApArticleConfig {

    private Long id;
    // 增加注解，JSON 序列化时自动混淆加密。
//    @JsonIgnore
    @IdEncrypt
    private Integer articleId;
    private Boolean isComment;
    private Boolean isForward;
    private Boolean isDown;
    private Boolean isDelete;

}
