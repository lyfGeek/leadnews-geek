//package com.geek.behavior.service.impl;
//
//import com.geek.behavior.kafka.BehaviorMessageSender;
//import com.geek.behavior.service.IAppLikesBehaviorService;
//import com.geek.common.kafka.messages.behavior.UserLikesMessage;
//import com.geek.common.zookeeper.sequence.Sequences;
//import com.geek.model.behavior.dtos.LikesBehaviorDto;
//import com.geek.model.behavior.pojos.ApBehaviorEntry;
//import com.geek.model.behavior.pojos.ApLikesBehavior;
//import com.geek.model.common.dtos.ResponseResult;
//import com.geek.model.common.enums.AppHttpCodeEnum;
//import com.geek.model.mappers.app.IApBehaviorEntryMapper;
//import com.geek.model.mappers.app.IApLikesBehaviorMapper;
//import com.geek.model.user.pojos.ApUser;
//import com.geek.utils.common.BurstUtils;
//import com.geek.utils.threadlocal.AppThreadLocalUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//@Service
//public class AppLikesBehaviorServiceImpl implements IAppLikesBehaviorService {
//
//    @Autowired
//    private IApBehaviorEntryMapper apBehaviorEntryMapper;
//
//    @Autowired
//    private IApLikesBehaviorMapper apLikesBehaviorMapper;
//
//    @Autowired
//    private Sequences sequences;
//
//    @Autowired
//    private BehaviorMessageSender behaviorMessageSender;
//
//    @Override
//    public ResponseResult saveLikesBehavior(LikesBehaviorDto dto) {
//
//        // 获取用户信息，获取设备 id。
//        ApUser user = AppThreadLocalUtils.getUser();
//        // 根据当前的用户信息或设备 id 查询行为实体 ap_behavior_entry。
//        if (user == null && dto.getEquipmentId() == null) {
//            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
//        }
//        Long userId = null;
//        if (user != null) {
//            userId = user.getId();
//        }
//        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipemntId(userId, dto.getEquipmentId());
//        if (apBehaviorEntry == null) {
//            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
//        }
//        ApLikesBehavior alb = new ApLikesBehavior();
//        alb.setId(sequences.sequenceApLikes());
//        alb.setBehaviorEntryId(apBehaviorEntry.getId());
//        alb.setCreatedTime(new Date());
//        alb.setEntryId(dto.getEntryId());
//        alb.setType(dto.getType());
//        alb.setOperation(dto.getOperation());
//        alb.setBurst(BurstUtils.encrypt(alb.getId(), alb.getBehaviorEntryId()));
//
//        int insert = apLikesBehaviorMapper.insert(alb);
//        if (insert == 1) {
//            if (alb.getOperation() == ApLikesBehavior.Operation.LIKE.getCode()) {
//                behaviorMessageSender.sendMessagePlus(new UserLikesMessage(alb), userId, true);
//            } else if (alb.getOperation() == ApLikesBehavior.Operation.CANCEL.getCode()) {
//                behaviorMessageSender.sendMessageReduce(new UserLikesMessage(alb), userId, true);
//            }
//        }
//        return ResponseResult.okResult(insert);
//    }
//
//}
