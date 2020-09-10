package com.geek.login.controller.v1;

import com.geek.login.service.IAppUserRelationService;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.user.dtos.UserRelationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
public class UserRelationController implements IUserRelationControllerApi {

    @Autowired
    private IAppUserRelationService appUserRelationService;

    @Override
    @PostMapping("/user_follow")
    public ResponseResult follow(@RequestBody UserRelationDto dto) {
        return appUserRelationService.follow(dto);
    }

}
