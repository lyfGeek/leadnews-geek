package com.geek.behavior.service;

import com.geek.model.behavior.dtos.ShowBehaviorDto;
import com.geek.model.common.dtos.ResponseResult;

public interface IAppShowBehaviorService {

    ResponseResult saveShowBehavior(ShowBehaviorDto dto);

}
