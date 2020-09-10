package com.geek.model.mappers.app;

import com.geek.model.article.pojos.ApHotWords;

import java.util.List;

public interface IApHotWordsMapper {

    /**
     * 查询今日热词。
     *
     * @param hotDate
     * @return
     */
    List<ApHotWords> queryByHotDate(String hotDate);

}
