package com.geek.model.behavior.dtos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class UnLikesBehaviorDto {

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
     * 不喜欢操作方式。
     * 0 ~ 不喜欢。
     * 1 ~ 取消不喜欢。
     */
    private Short type;

}
