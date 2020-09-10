package com.geek.model.mappers.app;

import com.geek.model.behavior.pojos.ApReadBehavior;
import org.apache.ibatis.annotations.Param;

public interface IApReadBehaviorMapper {

    int insert(ApReadBehavior record);

    int update(ApReadBehavior record);

    ApReadBehavior selectByEntryId(@Param("burst") String burst, @Param("entryId") Integer entryId, @Param("articleId") Integer articleId);

}
