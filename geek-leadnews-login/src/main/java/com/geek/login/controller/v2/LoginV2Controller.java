package com.geek.login.controller.v2;

import com.geek.admin.apis.ILoginControllerApi;
import com.geek.login.service.IApUserLoginService;
import com.geek.model.admin.pojos.AdUser;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.user.pojos.ApUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/login")
public class LoginV2Controller implements ILoginControllerApi {

    @Autowired
    private IApUserLoginService apUserLoginService;

    @Override
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody ApUser user) {
        return apUserLoginService.loginAuthV2(user);
    }

    @Override
    public ResponseResult login(AdUser user) {
        return null;
    }
}