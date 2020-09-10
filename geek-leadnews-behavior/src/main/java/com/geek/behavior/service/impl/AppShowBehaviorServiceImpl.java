package com.geek.behavior.service.impl;

import com.geek.behavior.service.IAppShowBehaviorService;
import com.geek.model.behavior.dtos.ShowBehaviorDto;
import com.geek.model.behavior.pojos.ApBehaviorEntry;
import com.geek.model.behavior.pojos.ApShowBehavior;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.common.enums.AppHttpCodeEnum;
import com.geek.model.mappers.app.IApBehaviorEntryMapper;
import com.geek.model.mappers.app.IApShowBehaviorMapper;
import com.geek.model.user.pojos.ApUser;
import com.geek.utils.threadlocal.AppThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AppShowBehaviorServiceImpl implements IAppShowBehaviorService {

    @Autowired
    private IApBehaviorEntryMapper apBehaviorEntryMapper;

    @Autowired
    private IApShowBehaviorMapper apShowBehaviorMapper;

    @Override
    public ResponseResult saveShowBehavior(ShowBehaviorDto dto) {
        // 获取用户信息（ThreadLocal 中获取） or 获取设备 id。
        ApUser user = AppThreadLocalUtils.getUser();
        // 根据当前的用户信息 or 设备 id 查询行为实体 ap_behavior_entry。
        if (user == null && dto.getEquipmentId() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
//            return ResponseResult.errorResult();
        }
        Long userId = null;
        // 用户不为空。赋值 userId。
        if (user != null) {
            userId = user.getId();
        }
        ApBehaviorEntry apBehaviorEntry = apBehaviorEntryMapper.selectByUserIdOrEquipmentId(userId, dto.getEquipmentId());
        if (apBehaviorEntry == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 获取前台传递过来的文章列表 id。
//        List<ApArticle> articleIds = dto.getArticleIds();
// selectListByEntryIdAndArticleIds(apBehaviorEntry.getId(), articleIds); 不能是集合，要是数组。
        Integer[] articleIds = new Integer[dto.getArticleIds().size()];
        for (int i = 0; i < articleIds.length; i++) {
            articleIds[i] = dto.getArticleIds().get(i).getId();
        }
        // 根据行为实体 id 和文章列表 id 查询 app 行为表 ap_show_behavior。
        List<ApShowBehavior> apShowBehaviorList = apShowBehaviorMapper
                .selectListByEntryIdAndArticleIds(apBehaviorEntry.getId(), articleIds);
        // 数据的过滤。需要删除前台传递过来的文章列表中已经存在的文章 id。
        List<Integer> integers = new ArrayList<>(Arrays.asList(articleIds));// 数组转集合，方便 remove();。
        if (!apShowBehaviorList.isEmpty()) {
            apShowBehaviorList.forEach(item -> {
                Integer articleId = item.getArticleId();
                integers.remove(articleId);
            });
        }
        // 保存。
        if (!integers.isEmpty()) {// 是否删除完了。删除完了就不必。
            articleIds = new Integer[integers.size()];
            integers.toArray(articleIds);// 从集合 integers 转数组 articleIds。
            apShowBehaviorMapper.saveShowBehavior(articleIds, apBehaviorEntry.getId());
        }
        return ResponseResult.okResult(0);
    }

}
