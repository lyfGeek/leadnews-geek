package com.geek.media.service;

import com.geek.model.admin.pojos.AdChannel;

import java.util.List;

public interface IAdChannelService {

    /**
     * 查询所有的频道。
     *
     * @return
     */
    List<AdChannel> selectAll();

}
