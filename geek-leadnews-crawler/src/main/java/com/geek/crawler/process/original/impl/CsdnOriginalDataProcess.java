package com.geek.crawler.process.original.impl;

import com.geek.crawler.config.CrawlerConfig;
import com.geek.crawler.process.entity.ProcessFlowData;
import com.geek.crawler.process.original.AbstractOriginalDataProcess;
import com.geek.model.crawler.core.parse.ParseItem;
import com.geek.model.crawler.core.parse.impl.CrawlerParseItem;
import com.geek.model.crawler.enums.CrawlerEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/*
process
n. （为达到某一目标的）过程；进程；（事物发展，尤指自然变化的）过程，步骤，流程；做事方法；工艺流程；工序
v. 加工；处理；审阅，审核，处理（文件、请求等）；数据处理
v. 列队行进；缓缓前进
 */
@Component
public class CsdnOriginalDataProcess extends AbstractOriginalDataProcess {

    @Autowired
    private CrawlerConfig crawlerConfig;

    /**
     * 将 url list 转换为对象。
     *
     * @param processFlowData
     * @return
     */
    @Override
    public List<ParseItem> parseOriginalRequestData(ProcessFlowData processFlowData) {
        List<ParseItem> parseItemList = null;
        List<String> initCrawlerUrlList = crawlerConfig.getInitCrawlerUrlList();
        if (initCrawlerUrlList != null && !initCrawlerUrlList.isEmpty()) {
            parseItemList = initCrawlerUrlList.stream().map(url -> {
                CrawlerParseItem crawlerParseItem = new CrawlerParseItem();
                url = url + "?rnd=" + System.currentTimeMillis();// 为了去重。
                crawlerParseItem.setUrl(url);
                crawlerParseItem.setDocumentType(CrawlerEnum.DocumentType.INIT.name());
                crawlerParseItem.setHandleType(processFlowData.getHandleType().name());
                return crawlerParseItem;
            }).collect(Collectors.toList());
        }
        return parseItemList;
    }

    /**
     * 获取优先级。
     *
     * @return
     */
    @Override
    public int getPriority() {
        return 10;
    }

}
