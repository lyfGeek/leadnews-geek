package com.geek.media.controller.v1;

import com.geek.admin.apis.ILoginControllerApi;
import com.geek.media.service.IUserLoginService;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.media.pojos.WmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController implements ILoginControllerApi {

    @Autowired
    private IUserLoginService userLoginService;

    @Override
    @RequestMapping("/in")
    public ResponseResult login(@RequestBody WmUser user) {
        return userLoginService.login(user);
    }

}
