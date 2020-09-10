package com.geek.model.mappers.wemedia;

import com.geek.model.media.dtos.StatisticDto;
import com.geek.model.media.pojos.WmNewsStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IWmNewsStatisticsMapper {

    List<WmNewsStatistics> findByTimeAndUserId(@Param("burst") String burst, @Param("userId") Long userId, @Param("dto") StatisticDto dto);
}
