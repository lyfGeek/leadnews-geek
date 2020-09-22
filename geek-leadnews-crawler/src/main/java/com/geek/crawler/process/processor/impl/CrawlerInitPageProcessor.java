package com.geek.crawler.process.processor.impl;

import com.geek.crawler.process.entity.CrawlerConfigProperty;
import com.geek.crawler.process.processor.AbstractCrawlerPageProcessor;
import com.geek.model.crawler.enums.CrawlerEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.util.List;

@Component
public class CrawlerInitPageProcessor extends AbstractCrawlerPageProcessor {

    @Autowired
    private CrawlerConfigProperty crawlerConfigProperty;

    @Override
    public void handlePage(Page page) {
        String initXpath = crawlerConfigProperty.getInitCrawlerXpath();
        List<String> helpUrlList = page.getHtml().xpath(initXpath).links().all();
        addSpiderRequest(helpUrlList, page.getRequest(), CrawlerEnum.DocumentType.HELP);
    }

    @Override
    public boolean isNeedHandleType(String handleType) {
        return CrawlerEnum.HandleType.FORWARD.name().equals(handleType);
    }

    @Override
    public boolean isNeedDocumentType(String documentType) {
        return CrawlerEnum.DocumentType.INIT.name().equals(documentType);
    }

    @Override
    public int getPriority() {
        return 100;
    }

}
