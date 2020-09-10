package com.geek.admin.job;

import com.geek.admin.service.IReviewCrawlerArticleService;
import com.geek.common.quartz.AbstractJob;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
@Log4j2
public class CrawlerReviewArticleJob extends AbstractJob {

    @Autowired
    private IReviewCrawlerArticleService reviewCrawlerArticleService;

    @Override
    public String[] triggerCron() {
        return new String[]{"0 0/1 * * * ?"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("开始定时任务执行");
        try {
            reviewCrawlerArticleService.autoReviewArticleByCrawler();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("定时任务结束，耗时：{}", System.currentTimeMillis() - currentTimeMillis);
    }

    @Override
    public String descTrigger() {
        return "每天晚上23:30分执行";
    }
}
