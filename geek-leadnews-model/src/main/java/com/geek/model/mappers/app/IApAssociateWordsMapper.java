package com.geek.model.mappers.app;

import com.geek.model.article.pojos.ApAssociateWords;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IApAssociateWordsMapper {

    /**
     * 根据关键词查询联想词。
     *
     * @param searchWords
     * @param limit
     * @return
     */
    List<ApAssociateWords> selectByAssociateWords(@Param("searchWords") String searchWords, @Param("limit") int limit);

    /**
     * 查询联想词。
     *
     * @return
     */
    List<ApAssociateWords> selectAllAssociateWords();

}
