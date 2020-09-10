package com.geek.model.mappers.app;

import com.geek.model.behavior.pojos.ApUnlikesBehavior;
import org.apache.ibatis.annotations.Param;

public interface IApUnlikesBehaviorMapper {

    /**
     * 选择最后一条不喜欢数据。
     *
     * @param entryId
     * @param articleId
     * @return
     */
    ApUnlikesBehavior selectLastUnLike(@Param("entryId") Integer entryId, @Param("articleId") Integer articleId);

    int insert(ApUnlikesBehavior record);

}
