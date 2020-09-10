package com.geek.migration.job;

import com.geek.common.quartz.AbstractJob;
import com.geek.migration.service.IArticleQuantityService;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
@Log4j2
public class MigrationDbToHBaseQuartz extends AbstractJob {
    @Autowired
    private IArticleQuantityService articleQuantityService;

    @Override
    public String[] triggerCron() {
        return new String[]{"0 0/2 * * * ?"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("开始同步文章数据到 HBase。");
        articleQuantityService.dbToHBase();
        log.info("同步文章数据到 HBase 完成。");
    }

}
