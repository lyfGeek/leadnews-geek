package com.geek.model.behavior.dtos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class FollowBehaviorDto {

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
    @IdEncrypt
    private Integer followId;

}
