package com.geek.media.service.impl;

import com.geek.common.media.constant.WmMediaConstant;
import com.geek.media.service.IStatisticsService;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.common.enums.AppHttpCodeEnum;
import com.geek.model.mappers.wemedia.IWmNewsStatisticsMapper;
import com.geek.model.mappers.wemedia.IWmUserMapper;
import com.geek.model.media.dtos.StatisticDto;
import com.geek.model.media.pojos.WmNewsStatistics;
import com.geek.model.media.pojos.WmUser;
import com.geek.utils.common.BurstUtils;
import com.geek.utils.threadlocal.WmThreadLocalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsServiceImpl implements IStatisticsService {

    @Autowired
    IWmUserMapper wmUserMapper;

    @Autowired
    IWmNewsStatisticsMapper wmNewsStatisticsMapper;

    @Override
    public ResponseResult findWmNewsStatistics(StatisticDto dto) {
        if (dto == null && dto.getType() == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (WmMediaConstant.WM_NEWS_STATISTIC_CUR != dto.getType() && (dto.getStime() == null || dto.getEtime() == null)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        // 查询用户。
        WmUser user = WmThreadLocalUtils.getUser();
        WmUser wmUser = wmUserMapper.selectById(user.getId());
        String burst = BurstUtils.groudOne(wmUser.getApUserId());
        List<WmNewsStatistics> list = wmNewsStatisticsMapper.findByTimeAndUserId(burst, wmUser.getApUserId(), dto);
        return ResponseResult.okResult(list);
    }

}
