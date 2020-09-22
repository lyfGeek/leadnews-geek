package com.geek.article.service;

import com.geek.model.article.dtos.UserSearchDto;
import com.geek.model.common.dtos.ResponseResult;

public interface IApArticleSearchService {

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
     * @param date
     * @return
     */
    ResponseResult hotkeywords(String date);

    /**
     * 模糊查询联想词。
     *
     * @param userSearchDto
     * @return
     */
    ResponseResult searchAssociate(UserSearchDto userSearchDto);

    /**
     * es 文章分页查询。
     *
     * @param dto
     * @return
     */
    ResponseResult esArticleSearch(UserSearchDto userSearchDto);

    /**
     * 保存搜索记录。
     *
     * @param entryId
     * @param searchWords
     * @return
     */
    ResponseResult saveUserSearch(Integer entryId, String searchWords);

    /**
     * 搜索联想词 v2。
     *
     * @param userSearchDto
     * @return
     */
    ResponseResult searchAssociationV2(UserSearchDto userSearchDto);

}
