package com.geek.crawler.process;

import com.geek.crawler.process.entity.ProcessFlowData;
import com.geek.model.crawler.enums.CrawlerEnum;

public interface IProcessFlow {

    /**
     * 处理主业务。
     *
     * @param processFlowData
     */
    void handle(ProcessFlowData processFlowData);

    /**
     * 获取抓取类型。
     *
     * @return
     */
    CrawlerEnum.ComponentType getComponentType();

    /**
     * 获取优先级。
     *
     * @return
     */
    int getPriority();

}
