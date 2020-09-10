package com.geek.login.service.impl;

import com.geek.login.service.IAppFollowBehaviorService;
import com.geek.model.behavior.dtos.FollowBehaviorDto;
import com.geek.model.behavior.pojos.ApBehaviorEntry;
import com.geek.model.behavior.pojos.ApFollowBehavior;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.common.enums.AppHttpCodeEnum;
import com.geek.model.mappers.app.IApBehaviorEntryMapper;
import com.geek.model.mappers.app.IApFollowBehaviorMapper;
import com.geek.model.user.pojos.ApUser;
import com.geek.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AppFollowBehaviorServiceImpl implements IAppFollowBehaviorService {

    @Autowired
    private IApBehaviorEntryMapper apBehaviorEntryMapper;

    @Autowired
    private IApFollowBehaviorMapper apFollowBehaviorMapper;

    @Override
    @Async
    public ResponseResult saveFollowBehavior(FollowBehaviorDto dto) {
//        int a = 1/0;
        ApUser user = AppThreadLocalUtils.getUser();
        if (user == null && dto.getEquipmentId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        Long userId = null;
        if (user != null) {
            userId = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, dto.getEquipmentId());
        if (apBehaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 保存行为。
        ApFollowBehavior alb = new ApFollowBehavior();
        alb.setEntryId(apBehaviorEntry.getId());
        alb.setArticleId(dto.getArticleId());
        alb.setFollowId(dto.getFollowId());
        alb.setCreatedTime(new Date());
        int insert = apFollowBehaviorMapper.insert(alb);
        return ResponseResult.okResult(insert);
    }

}
