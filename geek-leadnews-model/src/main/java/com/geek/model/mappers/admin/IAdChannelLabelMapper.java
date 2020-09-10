package com.geek.model.mappers.admin;

import com.geek.model.admin.pojos.AdChannelLabel;

public interface IAdChannelLabelMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(AdChannelLabel record);

    int insertSelective(AdChannelLabel record);

    AdChannelLabel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AdChannelLabel record);

    int updateByPrimaryKey(AdChannelLabel record);

    /**
     * 根据 labelId 查询。
     *
     * @param id
     * @return
     */
    AdChannelLabel selectByLabelId(Integer id);

}
