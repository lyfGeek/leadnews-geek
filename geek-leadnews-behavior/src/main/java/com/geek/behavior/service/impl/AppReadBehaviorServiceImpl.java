//package com.geek.behavior.service.impl;
//
//import com.geek.behavior.kafka.BehaviorMessageSender;
//import com.geek.behavior.service.IAppReadBehaviorService;
//import com.geek.common.kafka.messages.behavior.UserReadMessage;
//import com.geek.common.zookeeper.sequence.Sequences;
//import com.geek.model.behavior.dtos.ReadBehaviorDto;
//import com.geek.model.behavior.pojos.ApBehaviorEntry;
//import com.geek.model.behavior.pojos.ApReadBehavior;
//import com.geek.model.common.dtos.ResponseResult;
//import com.geek.model.common.enums.AppHttpCodeEnum;
//import com.geek.model.mappers.app.IApBehaviorEntryMapper;
//import com.geek.model.mappers.app.IApReadBehaviorMapper;
//import com.geek.model.user.pojos.ApUser;
//import com.geek.utils.common.BurstUtils;
//import com.geek.utils.threadlocal.AppThreadLocalUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//
//@Service
//public class AppReadBehaviorServiceImpl implements IAppReadBehaviorService {
//
//    @Autowired
//    private IApBehaviorEntryMapper apBehaviorEntryMapper;
//
//    @Autowired
//    private IApReadBehaviorMapper apReadBehaviorMapper;
//
//    @Autowired
//    private Sequences sequences;
//
//    @Autowired
//    private BehaviorMessageSender behaviorMessageSender;
//
//    @Override
//    public ResponseResult saveReadBehavior(ReadBehaviorDto dto) {
//
//        // 获取用户信息，获取设备 id。
//        ApUser user = AppThreadLocalUtils.getUser();
//        // 根据当前的用户信息或设备id查询行为实体 ap_behavior_entry。
//        if (user == null && dto.getEquipmentId() == null) {
//            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
//        }
//        Long userId = null;
//        if (user != null) {
//            userId = user.getId();
//        }
//        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, dto.getEquipmentId());
//        if (apBehaviorEntry == null) {
//            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
//        }
//        // 查询.
//        ApReadBehavior apReadBehavior = apReadBehaviorMapper.selectByEntryId(BurstUtils.groudOne(apBehaviorEntry.getId()), apBehaviorEntry.getId(), dto.getArticleId());
//        boolean isInsert = false;
//        if (apReadBehavior == null) {
//            isInsert = true;
//            apReadBehavior = new ApReadBehavior();
//            apReadBehavior.setId(sequences.sequenceApReadBehavior());
//        }
//        apReadBehavior.setEntryId(apBehaviorEntry.getId());
//        apReadBehavior.setCount(dto.getCount());
//        apReadBehavior.setPercentage(dto.getPercentage());
//        apReadBehavior.setLoadDuration(dto.getLoadDuration());
//        apReadBehavior.setArticleId(dto.getArticleId());
//        apReadBehavior.setUpdatedTime(new Date());
//        apReadBehavior.setCreatedTime(new Date());
//        apReadBehavior.setReadDuration(dto.getReadDuration());
//        apReadBehavior.setBurst(BurstUtils.encrypt(apReadBehavior.getId(), apReadBehavior.getEntryId()));
//        int count = 0;
//        if (isInsert) {
//            count = apReadBehaviorMapper.insert(apReadBehavior);
//            if (count == 1) {
//                behaviorMessageSender.sendMessagePlus(new UserReadMessage(apReadBehavior), userId, true);
//            }
//        } else {
//            count = apReadBehaviorMapper.update(apReadBehavior);
//        }
//
//        return ResponseResult.okResult(count);
//    }
//
//}
