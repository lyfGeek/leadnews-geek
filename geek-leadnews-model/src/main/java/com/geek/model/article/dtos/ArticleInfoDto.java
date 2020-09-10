package com.geek.model.article.dtos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class ArticleInfoDto {

    /**
     * 设备 ID。
     */
    @IdEncrypt
    private Integer equipmentId;
    /**
     * 文章 ID。
     */
    @IdEncrypt
    private Integer articleId;
    /**
     * 作者 ID。
     */
    @IdEncrypt
    private Integer authorId;

}
