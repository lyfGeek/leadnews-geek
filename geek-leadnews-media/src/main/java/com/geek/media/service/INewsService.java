package com.geek.media.service;

import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.media.dtos.WmNewsDto;
import com.geek.model.media.dtos.WmNewsPageReqDto;

public interface INewsService {

    /**
     * 保存或修改。
     *
     * @param dto
     * @param type
     * @return
     */
    ResponseResult saveNews(WmNewsDto dto, Short type);

    /**
     * 查询文章列表。
     *
     * @param dto
     * @return
     */
    ResponseResult listByUser(WmNewsPageReqDto dto);

    /**
     * 根据当前主键查询文章。
     *
     * @param dto
     * @return
     */
    ResponseResult findWmNewsById(WmNewsDto dto);

    /**
     * 根据主键删除文章信息。
     *
     * @param dto
     * @return
     */
    ResponseResult delNews(WmNewsDto dto);

}
