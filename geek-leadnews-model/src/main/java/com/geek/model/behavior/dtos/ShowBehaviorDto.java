package com.geek.model.behavior.dtos;

import com.geek.model.annotation.IdEncrypt;
import com.geek.model.article.pojos.ApArticle;
import lombok.Data;

import java.util.List;

@Data
public class ShowBehaviorDto {

    /**
     * 设备 ID。
     */
    @IdEncrypt
    Integer equipmentId;

    List<ApArticle> articleIds;

}
