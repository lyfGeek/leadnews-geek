package com.geek.media.service;

import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.media.pojos.WmUser;

public interface IUserLoginService {

    /**
     * 登录方法。
     */
    ResponseResult login(WmUser user);

}
