package com.geek.model.user.dtos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class UserRelationDto {

    /**
     * 文章作者 ID。
     */
    @IdEncrypt
    Integer authorId;

    /**
     * 用户 ID。
     */
    @IdEncrypt
    Integer userId;

    /**
     * 文章。
     */
    @IdEncrypt
    Integer articleId;

    /**
     * 操作方式。
     * 0 ~ 关注。
     * 1 ~ 取消。
     */
    Short operation;

}
