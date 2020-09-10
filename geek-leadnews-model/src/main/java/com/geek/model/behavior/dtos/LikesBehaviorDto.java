package com.geek.model.behavior.dtos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class LikesBehaviorDto {

    /**
     * 设备 ID。
     */
    @IdEncrypt
    private Integer equipmentId;
    /**
     * 文章、动态、评论等 ID。
     */
    @IdEncrypt
    private Integer entryId;
    /**
     * 喜欢内容类型。
     * 0 ~ 文章。
     * 1 ~ 动态。
     * 2 ~ 评论。
     */
    private Short type;
    /**
     * 喜欢操作方式。
     * 0 ~ 点赞。
     * 1 ~ 取消点赞。
     */
    private Short operation;

}
