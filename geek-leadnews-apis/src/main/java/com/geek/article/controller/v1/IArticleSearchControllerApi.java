package com.geek.article.controller.v1;

import com.geek.model.article.dtos.UserSearchDto;
import com.geek.model.common.dtos.ResponseResult;

public interface IArticleSearchControllerApi {

    /**
     * 查询搜索历史。
     *
     * @param userSearchDto
     * @return
     */
    ResponseResult findUserSearch(UserSearchDto userSearchDto);

    /**
     * 删除搜索历史。
     *
     * @param userSearchDto
     * @return
     */
    ResponseResult delUserSearch(UserSearchDto userSearchDto);

    /**
     * 清空搜索历史记录。
     *
     * @param userSearchDto
     * @return
     */
    ResponseResult clearUserSearch(UserSearchDto userSearchDto);

    /**
     * 今日热词。
     *
     * @param userSearchDto
     * @return
     */
    ResponseResult hotkeywords(UserSearchDto userSearchDto);

    /**
     * 模糊查询。
     *
     * @param userSearchDto
     * @return
     */
    ResponseResult searchAssociate(UserSearchDto userSearchDto);

    /**
     * es 分页查询。
     *
     * @param userSearchDto
     * @return
     */
    ResponseResult esArticleSearch(UserSearchDto userSearchDto);

}
