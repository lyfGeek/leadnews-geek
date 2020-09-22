package com.geek.admin.apis;

import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.user.pojos.ApUser;

public interface ILoginControllerApi {

    /**
     * 登录。
     *
     * @param apUser
     * @return
     */
    ResponseResult login(ApUser apUser);

}
