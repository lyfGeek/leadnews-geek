package com.geek.model.mappers.admin;

import com.geek.model.admin.pojos.AdUser;

public interface IAdUserMapper {

    AdUser selectByName(String name);

}
