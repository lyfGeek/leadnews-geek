package com.geek.model.mappers.app;

import com.geek.model.user.pojos.ApUserFollow;
import org.apache.ibatis.annotations.Param;

public interface IApUserFollowMapper {

    ApUserFollow selectByFollowId(@Param("burst") String burst, @Param("userId") Long userId, @Param("followId") Integer followId);

    int insert(ApUserFollow record);

    int deleteByFollowId(@Param("burst") String burst, @Param("userId") Long userId, @Param("followId") Integer followId);

}
