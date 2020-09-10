package com.geek.model.behavior.dtos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

import java.util.Date;

@Data
public class CollectionBehaviorDto {

    /**
     * 设备 ID。
     */
    @IdEncrypt
    private Integer equipmentId;
    /**
     * 文章、动态 ID。
     */
    @IdEncrypt
    private Integer entryId;
    /**
     * 收藏内容类型。
     * 0 ~ 文章。
     * 1 ~ 动态。
     */
    private Short type;
    /**
     * 操作类型。
     * 0 ~ 收藏。
     * 1 ~ 取消收藏。
     */
    private Short operation;

    private Date publishedTime;

}
