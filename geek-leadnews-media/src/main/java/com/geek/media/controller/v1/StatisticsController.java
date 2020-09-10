package com.geek.media.controller.v1;

import com.geek.media.service.IStatisticsService;
import com.geek.model.common.dtos.ResponseResult;
import com.geek.model.media.dtos.StatisticDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    @Autowired
    IStatisticsService statisticsService;

    @PostMapping("/news")
    public ResponseResult newsData(@RequestBody StatisticDto dto) {
        return statisticsService.findWmNewsStatistics(dto);
    }

}
