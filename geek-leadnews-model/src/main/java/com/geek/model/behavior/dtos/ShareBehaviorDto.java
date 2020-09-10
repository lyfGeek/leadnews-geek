package com.geek.model.behavior.dtos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class ShareBehaviorDto {

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
     * 分享渠道。
     * 0 微信。
     * 1 微信朋友圈。
     * 2 QQ。
     * 3 QQ 空间。
     * 4 微博。
     */
    private Short type;

}
