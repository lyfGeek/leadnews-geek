package com.geek.login.service.impl;

import com.geek.login.service.IApUserLoginService;
import com.geek.login.service.IValidateService;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.common.enums.AppHttpCodeEnum;
import com.geek.model.mappers.app.IApUserMapper;
import com.geek.model.user.pojos.ApUser;
import com.geek.utils.jwt.AppJwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ApUserLoginServiceImpl implements IApUserLoginService {

    @Autowired
    private IApUserMapper apUserMapper;
    @Autowired
    private IValidateService validateService;

    @Override
    public ResponseResult loginAuth(ApUser user) {
        // 验证参数。
        if (StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        ApUser apUser = apUserMapper.selectByApPhone(user.getPhone());
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }
        if (!user.getPassword().equals(apUser.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        apUser.setPassword("");
        Map<String, Object> map = new HashMap<>();
        map.put("token", AppJwtUtil.getToken(apUser));
        map.put("user", apUser);

        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult loginAuthV2(ApUser user) {
        // 验证参数
        if (StringUtils.isEmpty(user.getPhone()) || StringUtils.isEmpty(user.getPassword())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 查询用户。
        ApUser dbUser = apUserMapper.selectByApPhone(user.getPhone());
        if (dbUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.AP_USER_DATA_NOT_EXIST);
        }
        // 验证密码。
        boolean isValid = validateService.validateMD5(user, dbUser);
        if (!isValid) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        user.setPassword("");
        Map<String, Object> map = new HashMap<>();
        map.put("token", AppJwtUtil.getToken(user));
        map.put("user", user);

        return ResponseResult.okResult(map);
    }

}
