package com.geek.media.controller.v1;

import com.geek.media.service.IAdChannelService;
import com.geek.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/channel")
public class AdChannelController implements IAdChannelControllerApi {

    @Autowired
    private IAdChannelService adChannelService;

    @Override
    @RequestMapping("/channels")
    public ResponseResult selectAll() {
        return ResponseResult.okResult(adChannelService.selectAll());
    }

}
