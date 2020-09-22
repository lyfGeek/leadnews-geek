package com.geek.login.service;

import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.user.pojos.ApUser;

public interface IApUserLoginService {

    /**
     * 根据用户名和密码登录验证。
     *
     * @param user
     * @return
     */
    ResponseResult loginAuth(ApUser user);

    /**
     * 根据用户名和密码登录验证。V2。
     *
     * @param user
     * @return
     */
    ResponseResult loginAuthV2(ApUser user);

}
