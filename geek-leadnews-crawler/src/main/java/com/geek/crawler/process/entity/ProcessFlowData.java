package com.geek.crawler.process.entity;

import com.geek.model.crawler.core.parse.ParseItem;
import com.geek.model.crawler.enums.CrawlerEnum;
import lombok.Data;

import java.util.List;

/**
 * 流程数据。
 */
@Data
public class ProcessFlowData {

    /**
     * 抓取对象列表。
     */
    private List<ParseItem> parseItemList;
    /**
     * 处理类型。
     */
    private CrawlerEnum.HandleType handleType = CrawlerEnum.HandleType.FORWARD;

}
