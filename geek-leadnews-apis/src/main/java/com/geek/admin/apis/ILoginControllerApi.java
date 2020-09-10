package com.geek.admin.apis;

import com.geek.model.admin.pojos.AdUser;
import com.geek.model.common.dtos.ResponseResult;

public interface ILoginControllerApi {

    /**
     * 登录。
     *
     * @param user
     * @return
     */
    ResponseResult login(AdUser user);

}
