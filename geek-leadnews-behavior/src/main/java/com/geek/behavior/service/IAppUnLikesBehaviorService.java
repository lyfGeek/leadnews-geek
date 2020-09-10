package com.geek.behavior.service;

import com.geek.model.behavior.dtos.UnLikesBehaviorDto;
import com.geek.model.common.dtos.ResponseResult;

public interface IAppUnLikesBehaviorService {

    /**
     * 保存不喜欢数据。
     *
     * @param dto
     * @return
     */
    ResponseResult saveUnLikesBehavior(UnLikesBehaviorDto dto);

}
