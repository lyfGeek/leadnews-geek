package com.geek.model.mappers.wemedia;

import com.geek.model.media.pojos.WmUser;

public interface IWmUserMapper {

    WmUser selectByName(String name);

    WmUser selectById(Long id);

}
