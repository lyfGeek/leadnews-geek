package com.geek.crawler.process.processor;

import com.geek.crawler.helper.CrawlerHelper;
import com.geek.crawler.process.IProcessFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;

/**
 * 爬虫文档流程管理。
 */
@Component
public class CrawlerPageProcessorManager {

    @Autowired
    private CrawlerHelper crawlerHelper;

    @Resource
    private List<AbstractCrawlerPageProcessor> abstractCrawlerPageProcessorList;

    /**
     * 初始化注入接口顺序。
     */
    @PostConstruct
    public void initProcessingFlow() {
        if (abstractCrawlerPageProcessorList != null && !abstractCrawlerPageProcessorList.isEmpty()) {
            abstractCrawlerPageProcessorList.sort((Comparator<IProcessFlow>) (o1, o2) -> {
                if (o1.getPriority() > o2.getPriority()) {
                    return 1;
                } else if (o1.getPriority() > o2.getPriority()) {
                    return -1;
                }
                return 0;
            });
        }
    }

    /**
     * 处理数据。
     *
     * @param page
     */
    public void handle(Page page) {
        String handleType = crawlerHelper.getHandleType(page.getRequest());
        String documentType = crawlerHelper.getDocumentType(page.getRequest());
        for (AbstractCrawlerPageProcessor abstractCrawlerPageProcessor : abstractCrawlerPageProcessorList) {
            boolean needHandleType = abstractCrawlerPageProcessor.isNeedHandleType(handleType);
            boolean needDocumentType = abstractCrawlerPageProcessor.isNeedDocumentType(documentType);
            if (needHandleType && needDocumentType) {
                abstractCrawlerPageProcessor.handlePage(page);
            }
        }
    }

}
