package com.geek.admin.service;

import com.geek.model.admin.pojos.AdUser;
import com.geek.model.common.dtos.ResponseResult;

public interface IUserLoginService {

    /**
     * 登录。
     *
     * @param user
     * @return
     */
    ResponseResult login(AdUser user);

}
