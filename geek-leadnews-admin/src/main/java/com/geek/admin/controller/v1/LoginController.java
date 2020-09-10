package com.geek.admin.controller.v1;


import com.geek.admin.apis.ILoginControllerApi;
import com.geek.admin.service.IUserLoginService;
import com.geek.model.admin.pojos.AdUser;
import com.geek.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController implements ILoginControllerApi {

    @Autowired
    private IUserLoginService userLoginService;

    @Override
    @PostMapping("/in")
    public ResponseResult login(@RequestBody AdUser user) {
        return userLoginService.login(user);
    }
}
