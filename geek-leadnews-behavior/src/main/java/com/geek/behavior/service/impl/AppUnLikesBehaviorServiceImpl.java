package com.geek.behavior.service.impl;

import com.geek.behavior.service.IAppUnLikesBehaviorService;
import com.geek.model.behavior.dtos.UnLikesBehaviorDto;
import com.geek.model.behavior.pojos.ApBehaviorEntry;
import com.geek.model.behavior.pojos.ApUnlikesBehavior;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.common.enums.AppHttpCodeEnum;
import com.geek.model.mappers.app.IApBehaviorEntryMapper;
import com.geek.model.mappers.app.IApUnlikesBehaviorMapper;
import com.geek.model.user.pojos.ApUser;
import com.geek.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AppUnLikesBehaviorServiceImpl implements IAppUnLikesBehaviorService {

    @Autowired
    private IApBehaviorEntryMapper apBehaviorEntryMapper;

    @Autowired
    private IApUnlikesBehaviorMapper apUnlikesBehaviorMapper;

    @Override
    public ResponseResult saveUnLikesBehavior(UnLikesBehaviorDto dto) {
        // 获取用户信息，获取设备 id。
        ApUser user = AppThreadLocalUtils.getUser();
        // 根据当前的用户信息或设备 id 查询行为实体 ap_behavior_entry。
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

        ApUnlikesBehavior alb = new ApUnlikesBehavior();
        alb.setEntryId(apBehaviorEntry.getId());
        alb.setCreatedTime(new Date());
        alb.setArticleId(dto.getArticleId());
        alb.setType(dto.getType());
        int insert = apUnlikesBehaviorMapper.insert(alb);

        return ResponseResult.okResult(insert);
    }

}
