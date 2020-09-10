package com.geek.model.websocket.dtos;

import com.geek.model.annotation.IdEncrypt;
import lombok.Data;

@Data
public class WebSocketDto {

    /**
     * 设备 ID。
     */
    @IdEncrypt
    Integer equipmentId;
    /**
     * 文章 ID。
     */
    @IdEncrypt
    String token;

}
