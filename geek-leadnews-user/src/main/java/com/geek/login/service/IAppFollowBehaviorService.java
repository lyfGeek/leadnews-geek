package com.geek.login.service;

import com.geek.model.behavior.dtos.FollowBehaviorDto;
import com.geek.model.common.dtos.ResponseResult;

public interface IAppFollowBehaviorService {

    /**
     * 存储关注行为数据。
     */
    ResponseResult saveFollowBehavior(FollowBehaviorDto dto);

}
