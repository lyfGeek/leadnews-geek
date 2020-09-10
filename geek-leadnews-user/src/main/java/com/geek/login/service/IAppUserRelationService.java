package com.geek.login.service;

import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.user.dtos.UserRelationDto;

public interface IAppUserRelationService {

    /**
     * 用户的关注或取消关注。
     *
     * @param dto
     * @return
     */
    ResponseResult follow(UserRelationDto dto);

}
