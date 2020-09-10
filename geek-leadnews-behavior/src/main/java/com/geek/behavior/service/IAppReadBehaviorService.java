package com.geek.behavior.service;

import com.geek.model.behavior.dtos.ReadBehaviorDto;
import com.geek.model.common.dtos.ResponseResult;

public interface IAppReadBehaviorService {

    /**
     * 存储阅读数据。
     *
     * @param dto
     * @return
     */
    ResponseResult saveReadBehavior(ReadBehaviorDto dto);

}
