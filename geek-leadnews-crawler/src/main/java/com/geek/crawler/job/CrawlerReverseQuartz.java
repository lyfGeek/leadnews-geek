package com.geek.crawler.job;

import com.geek.common.quartz.AbstractJob;
import com.geek.crawler.manager.ProcessingFlowManager;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
@Log4j2
public class CrawlerReverseQuartz extends AbstractJob {

    @Autowired
    private ProcessingFlowManager processingFlowManager;

    @Override
    public String[] triggerCron() {
        return new String[]{"0 0 0/1 * * ?"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long currentTime = System.currentTimeMillis();
        log.info("开始反向抓取。");
//        processingFlowManager.reverseHandel();
        log.info("反向抓取结束，耗时：", System.currentTimeMillis() - currentTime);
    }

}
