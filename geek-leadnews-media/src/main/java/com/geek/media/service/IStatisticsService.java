package com.geek.media.service;

import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.media.dtos.StatisticDto;

public interface IStatisticsService {

    /**
     * 查找图文统计数据。
     *
     * @param dto
     * @return
     */
    ResponseResult findWmNewsStatistics(StatisticDto dto);

}
