package com.geek.crawler.manager;

import com.geek.crawler.config.CrawlerConfig;
import com.geek.crawler.process.IProcessFlow;
import com.geek.crawler.process.entity.CrawlerComponent;
import com.geek.crawler.process.entity.ProcessFlowData;
import com.geek.model.crawler.core.parse.ParseItem;
import com.geek.model.crawler.enums.CrawlerEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.Scheduler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Component
@Log4j2
public class ProcessingFlowManager {

    @Autowired
    private CrawlerConfig crawlerConfig;

    @Resource
    private List<IProcessFlow> processFlowList;

//    @Autowired
//    private ICrawlerNewsAdditionalService crawlerNewsAdditionalService;

    /**
     * spring 启动的时候初始化方法。
     * 通过子类的优先级进行排序。
     * 初始化 spider。
     */
    @PostConstruct
    public void initProcessingFlow() {
        if (null != processFlowList && !processFlowList.isEmpty()) {
            processFlowList.sort((o1, o2) -> {
                if (o1.getPriority() > o2.getPriority()) {
                    return 1;
                } else if (o1.getPriority() < o2.getPriority()) {
                    return -1;
                }
                return 0;
            });
        }
        Spider spider = configSpider();
        crawlerConfig.setSpider(spider);
    }

    private Spider configSpider() {
        Spider spider = initSpider();
        spider.thread(5);
        return spider;
    }

    /**
     * 根据 IProcessFlow 接口的 getComponentType(); 接口类型生成 Spider 对象。
     *
     * @return
     */
    private Spider initSpider() {
        Spider spider = null;
        CrawlerComponent crawlerComponent = getComponent(processFlowList);
        if (null != crawlerComponent) {
            PageProcessor pageProcessor = crawlerComponent.getPageProcessor();
            if (pageProcessor != null) {
                spider = Spider.create(pageProcessor);
            }
            if (null != spider && null != crawlerComponent.getScheduler()) {
                spider.setScheduler(crawlerComponent.getScheduler());
            }
            if (null != spider && null != crawlerComponent.getDownloader()) {
                spider.setDownloader(crawlerComponent.getDownloader());
            }
            List<Pipeline> pipelineList = crawlerComponent.getPipelineList();
            if (null != spider && null != pipelineList && !pipelineList.isEmpty()) {
                for (Pipeline pipeline : pipelineList) {
                    spider.addPipeline(pipeline);
                }
            }
        }
        return spider;
    }

    /**
     * 抓取组件的封装。
     *
     * @param processFlowList
     * @return
     */
    private CrawlerComponent getComponent(List<IProcessFlow> processFlowList) {
        CrawlerComponent crawlerComponent = new CrawlerComponent();
        for (IProcessFlow processFlow : processFlowList) {
            if (processFlow.getComponentType() == CrawlerEnum.ComponentType.PAGEPROCESSOR) {
                crawlerComponent.setPageProcessor((PageProcessor) processFlow);
            } else if (processFlow.getComponentType() == CrawlerEnum.ComponentType.PIPELINE) {
                crawlerComponent.addPipeline((Pipeline) processFlow);
            } else if (processFlow.getComponentType() == CrawlerEnum.ComponentType.DOWNLOAD) {
                crawlerComponent.setDownloader((Downloader) processFlow);
            } else if (processFlow.getComponentType() == CrawlerEnum.ComponentType.SCHEDULER) {
                crawlerComponent.setScheduler((Scheduler) processFlow);
            }
        }
        return crawlerComponent;
    }

    /**
     * 开始处理爬虫任务。
     *
     * @param parseItemList
     * @param handleType
     */
    public void startTask(List<ParseItem> parseItemList, CrawlerEnum.HandleType handleType) {
        ProcessFlowData processFlowData = new ProcessFlowData();
        processFlowData.setHandleType(handleType);
        processFlowData.setParseItemList(parseItemList);
        for (IProcessFlow processFlow : processFlowList) {
            processFlow.handle(processFlowData);
        }
        crawlerConfig.getSpider().start();
    }

    /**
     * 逆向处理。
     */
    public void reverseHandle() {
//        List<ParseItem> parseItemList = crawlerNewsAdditionalService.queryIncrementParseItem(new Date());
//        handleReverseData(parseItemList);
//        log.info("开始进行数据逆向更新，增量数据数量为：{}", parseItemList.size());
//        if (null != parseItemList && !parseItemList.isEmpty()) {
//            startTask(parseItemList, CrawlerEnum.HandleType.REVERSE);
//        } else {
//            log.info("增量数据为空不能进行数据更新。");
//        }
    }

    /**
     * 逆向同步数据。
     *
     * @param parseItemList
     */
    private void handleReverseData(List<ParseItem> parseItemList) {
        if (null != parseItemList && !parseItemList.isEmpty()) {
            for (ParseItem parseItem : parseItemList) {
                parseItem.setDocumentType(CrawlerEnum.DocumentType.PAGE.name());
                parseItem.setHandleType(CrawlerEnum.HandleType.REVERSE.name());
            }
        }
    }

    /**
     * 正向爬取。
     */
    public void handle() {
        startTask(null, CrawlerEnum.HandleType.FORWARD);
    }

}
