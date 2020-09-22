package com.geek.login.service.impl;

import com.geek.common.zookeeper.sequence.Sequences;
import com.geek.login.service.IAppFollowBehaviorService;
import com.geek.login.service.IAppUserRelationService;
import com.geek.model.article.pojos.ApAuthor;
import com.geek.model.behavior.dtos.FollowBehaviorDto;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.common.enums.AppHttpCodeEnum;
import com.geek.model.mappers.app.IApAuthorMapper;
import com.geek.model.mappers.app.IApUserFanMapper;
import com.geek.model.mappers.app.IApUserFollowMapper;
import com.geek.model.mappers.app.IApUserMapper;
import com.geek.model.user.dtos.UserRelationDto;
import com.geek.model.user.pojos.ApUser;
import com.geek.model.user.pojos.ApUserFan;
import com.geek.model.user.pojos.ApUserFollow;
import com.geek.utils.common.BurstUtils;
import com.geek.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AppUserRelationServiceImpl implements IAppUserRelationService {

    @Autowired
    private IApAuthorMapper apAuthorMapper;
    @Autowired
    private IApUserMapper apUserMapper;
    @Autowired
    private IApUserFollowMapper apUserFollowMapper;
    @Autowired
    private IApUserFanMapper apUserFanMapper;
    @Autowired
    private Sequences sequences;
    @Autowired
    private IAppFollowBehaviorService appFollowBehaviorService;

    @Override
    public ResponseResult follow(UserRelationDto dto) {
        if (dto.getOperation() == null || dto.getOperation() < 0 || dto.getOperation() > 1) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "无效的operation参数");
        }

        // 获取到 followId。
        Integer followId = dto.getUserId();
        if (followId == null && dto.getAuthorId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE, "followId或authorId不能同时为空");
        } else if (followId == null) {
            ApAuthor apAuthor = apAuthorMapper.selectById(dto.getAuthorId());
            if (apAuthor != null) {
                followId = apAuthor.getUserId().intValue();
            }
        }
        if (followId == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "关注人不存在");
        } else {
            // 判断当前用户是否已经关注。
            ApUser user = AppThreadLocalUtils.getUser();
            if (user != null) {
                if (dto.getOperation() == 0) {
                    //关注操作。
                    // 保存 ap_user_follow、ap_user_fan 保存关注的行为。
                    return followByUserId(user, followId, dto.getArticleId());
                } else {
                    // 取消关注。
                    // 删除 ap_user_follow、ap_user_fan。
                    return followCancelByUserId(user, followId);

                }
            } else {
                return ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            }
        }
    }

    /**
     * 处理关注的逻辑。
     * 保存 ap_user_follow  ap_user_fan。
     * 保存关注的行为。
     *
     * @param user
     * @param followId
     * @param articleId
     * @return
     */
    private ResponseResult followByUserId(ApUser user, Integer followId, Integer articleId) {
        // 判断用户是否存在。
        ApUser apUser = apUserMapper.selectById(followId);
        if (apUser == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "关注用户不存在");
        }
        ApUserFollow apUserFollow = apUserFollowMapper.selectByFollowId(BurstUtils.groundOne(user.getId()), user.getId(), followId);
        if (apUserFollow == null) {
            ApUserFan apUserFan = apUserFanMapper.selectByFansId(BurstUtils.groundOne(followId), followId, user.getId());
            if (apUserFan == null) {
                apUserFan = new ApUserFan();
                apUserFan.setId(sequences.sequenceApUserFan());
                apUserFan.setUserId(followId);
                apUserFan.setFansId(user.getId());
                apUserFan.setFansName(user.getName());
                apUserFan.setLevel((short) 0);
                apUserFan.setIsDisplay(true);
                apUserFan.setIsShieldComment(false);
                apUserFan.setIsShieldLetter(false);
                apUserFan.setBurst(BurstUtils.encrypt(apUserFan.getId(), apUserFan.getUserId()));
                apUserFanMapper.insert(apUserFan);
            }
            apUserFollow = new ApUserFollow();
            apUserFollow.setId(sequences.sequenceApUserFollow());
            apUserFollow.setUserId(user.getId());
            apUserFollow.setFollowId(followId);
            apUserFollow.setFollowName(apUser.getName());
            apUserFollow.setCreatedTime(new Date());
            apUserFollow.setLevel((short) 0);
            apUserFollow.setIsNotice(true);
            apUserFollow.setBurst(BurstUtils.encrypt(apUserFollow.getId(), apUserFollow.getUserId()));
            // 记录用户关注的行为。
            FollowBehaviorDto dto = new FollowBehaviorDto();
            dto.setFollowId(followId);
            dto.setArticleId(articleId);

            appFollowBehaviorService.saveFollowBehavior(dto);
            int insert = apUserFollowMapper.insert(apUserFollow);
            return ResponseResult.okResult(insert);

        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST, "已关注");
        }
    }

    /**
     * 处理取消关注的逻辑。
     *
     * @param user
     * @param followId
     * @return
     */
    private ResponseResult followCancelByUserId(ApUser user, Integer followId) {
        ApUserFollow apUserFollow = apUserFollowMapper.selectByFollowId(BurstUtils.groundOne(user.getId()), user.getId(), followId);
        if (apUserFollow == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST, "未关注");
        } else {
            ApUserFan apUserFan = apUserFanMapper.selectByFansId(BurstUtils.groundOne(followId), followId, user.getId());
            if (apUserFan != null) {
                apUserFanMapper.deleteByFansId(BurstUtils.groundOne(followId), followId, user.getId());
            }
            int count = apUserFollowMapper.deleteByFollowId(BurstUtils.groundOne(user.getId()), user.getId(), followId);
            return ResponseResult.okResult(count);
        }
    }

}
