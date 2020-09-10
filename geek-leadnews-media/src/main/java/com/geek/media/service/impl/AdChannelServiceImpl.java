package com.geek.media.service.impl;

import com.geek.media.service.IAdChannelService;
import com.geek.model.admin.pojos.AdChannel;
import com.geek.model.mappers.admin.IAdChannelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdChannelServiceImpl implements IAdChannelService {

    @Autowired
    private IAdChannelMapper adChannelMapper;

    @Override
    public List<AdChannel> selectAll() {
        return adChannelMapper.selectAll();
    }

}
