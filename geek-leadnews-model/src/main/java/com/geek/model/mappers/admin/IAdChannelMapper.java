package com.geek.model.mappers.admin;

import com.geek.model.admin.pojos.AdChannel;

import java.util.List;

public interface IAdChannelMapper {

    /**
     * 查询所有。
     */
    List<AdChannel> selectAll();

    AdChannel selectByPrimaryKey(Integer id);

}
