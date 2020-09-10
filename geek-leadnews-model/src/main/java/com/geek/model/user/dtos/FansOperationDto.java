package com.geek.model.user.dtos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class FansOperationDto {

    @IdEncrypt
    private Integer fansId;

    /**
     * true 开。
     * false 关。
     */
    private Boolean switchState;

}
