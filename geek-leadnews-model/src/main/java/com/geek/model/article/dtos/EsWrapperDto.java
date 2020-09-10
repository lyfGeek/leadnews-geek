package com.geek.model.article.dtos;

import lombok.Data;

@Data
public class EsWrapperDto {

    /**
     * 存储 ID。
     */
    private Integer id;
    /**
     * 存储类型。
     */
    private String tag;

}