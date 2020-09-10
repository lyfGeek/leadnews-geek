package com.geek.model.mappers.app;

import com.geek.model.user.pojos.ApUser;

public interface IApUserMapper {

    ApUser selectById(Integer id);

    ApUser selectByApPhone(String phone);

}
