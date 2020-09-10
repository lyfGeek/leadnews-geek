package com.geek.model.mappers.app;

import com.geek.model.user.pojos.ApUserFan;
import org.apache.ibatis.annotations.Param;

public interface IApUserFanMapper {

    int insert(ApUserFan record);

    ApUserFan selectByFansId(@Param("burst") String burst, @Param("userId") Integer userId, @Param("fansId") Long fansId);

    int deleteByFansId(@Param("burst") String burst, @Param("userId") Integer userId, @Param("fansId") Long fansId);

}
