package com.geek.crawler.process.processor.impl;

import com.geek.crawler.helper.CrawlerHelper;
import com.geek.crawler.process.entity.CrawlerConfigProperty;
import com.geek.crawler.process.processor.AbstractCrawlerPageProcessor;
import com.geek.crawler.utils.ParseRuleUtils;
import com.geek.model.crawler.core.parse.ParseRule;
import com.geek.model.crawler.enums.CrawlerEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.util.List;

@Component
@Log4j2
public class CrawlerDocPageProcessor extends AbstractCrawlerPageProcessor {

    @Autowired
    private CrawlerConfigProperty crawlerConfigProperty;

    @Autowired
    private CrawlerHelper crawlerHelper;

    /**
     * 处理页面数据。
     *
     * @param page
     */
    @Override
    public void handlePage(Page page) {
        long currentTime = System.currentTimeMillis();
        String handleType = crawlerHelper.getHandleType(page.getRequest());
        log.info("开始解析目标页数。url：{}，handleType：{}", page.getUrl(), handleType);
        // 获取目标页的抓取规则。
        List<ParseRule> targetParseRuleList = crawlerConfigProperty.getTargetParseRuleList();
        // 抽取有效的数据。
        targetParseRuleList = ParseRuleUtils.parseHtmlByRuleList(page.getHtml(), targetParseRuleList);
        if (null != targetParseRuleList && !targetParseRuleList.isEmpty()) {
            for (ParseRule parseRule : targetParseRuleList) {
                // 将数据添加到 page 中，交给后续的 pipeline 处理。
                log.info("添加数据字段到 page 中的 field。url：{}，handleType：{}，field：{}", page.getUrl(), handleType, parseRule.getField());
                page.putField(parseRule.getField(), parseRule.getMergeContent());
            }
        }
        log.info("解析目标数据完成。url：{}，handleType：{}，耗时：{}", page.getUrl(), handleType, System.currentTimeMillis() - currentTime);
    }

    @Override
    public boolean isNeedHandleType(String handleType) {
        return CrawlerEnum.HandleType.FORWARD.name().equals(handleType);
    }

    @Override
    public boolean isNeedDocumentType(String documentType) {
        return CrawlerEnum.DocumentType.PAGE.name().equals(documentType);
    }

    @Override
    public int getPriority() {
        return 120;
    }

}
