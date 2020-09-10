package com.geek.model.behavior.dtos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class ReadBehaviorDto {

    /**
     * 设备 ID。
     */
    @IdEncrypt
    private Integer equipmentId;
    /**
     * 文章、动态、评论等 ID。
     */
    @IdEncrypt
    private Integer articleId;
    /**
     * 阅读次数。
     */
    private Short count;
    /**
     * 阅读时长（S)。
     */
    private Integer readDuration;
    /**
     * 阅读百分比。
     */
    private Short percentage;
    /**
     * 加载时间。
     */
    private Short loadDuration;

}
