package com.geek.model.article.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleHomeDto {

    /**
     * 省市。
     */
    private Integer provinceId;
    /**
     * 市区。
     */
    private Integer cityId;
    /**
     * 区县。
     */
    private Integer countyId;
    /**
     * 最大时间。
     */
    private Date maxBehotTime;
    /**
     * 最小时间。
     */
    private Date minBehotTime;
    /**
     * 分页 size。
     */
    private Integer size;
    /**
     * 数据范围。比如频道 ID。
     */
    private String tag;

}
