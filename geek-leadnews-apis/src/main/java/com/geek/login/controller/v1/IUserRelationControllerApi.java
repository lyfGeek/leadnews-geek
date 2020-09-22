package com.geek.login.controller.v1;

import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.user.dtos.UserRelationDto;

public interface IUserRelationControllerApi {

    ResponseResult follow(UserRelationDto dto);

}
