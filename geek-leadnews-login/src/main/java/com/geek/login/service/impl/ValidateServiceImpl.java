package com.geek.login.service.impl;

import com.geek.login.service.IValidateService;
import com.geek.model.user.pojos.ApUser;
import com.geek.utils.common.DESUtils;
import com.geek.utils.common.MD5Utils;
import org.springframework.stereotype.Service;

@Service
public class ValidateServiceImpl implements IValidateService {

    @Override
    public boolean validateDES(ApUser user, ApUser db) {
        return db.getPassword().equals(DESUtils.encode(user.getPassword()));
    }

    @Override
    public boolean validateMD5(ApUser user, ApUser db) {
        return db.getPassword().equals(MD5Utils.encode(user.getPassword()));
    }

    @Override
    public boolean validateWithSalt(ApUser user, ApUser db) {
        return db.getPassword().equals(MD5Utils.encodeWithSalt(user.getPassword(), db.getSalt()));
    }

}
