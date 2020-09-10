package com.geek.migration.job;

import com.geek.common.quartz.AbstractJob;
import com.geek.migration.service.IApHotArticleService;
import com.geek.model.article.pojos.ApHotArticles;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
@DisallowConcurrentExecution
public class MigrationDeleteHotDataQuartz extends AbstractJob {

    @Autowired
    private IApHotArticleService apHotArticleService;

    @Override
    public String[] triggerCron() {
        return new String[]{"0 30 22 * * ?"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("开始删除数据库过期数据。");
        deleteExpireHotData();
        log.info("删除数据库过期数据结束，耗时：{}", System.currentTimeMillis() - currentTimeMillis);
    }

    /**
     * 删除过期数据。
     */
    private void deleteExpireHotData() {
        List<ApHotArticles> hotArticlesList = apHotArticleService.selectExpireMonth();
        if (null != hotArticlesList && !hotArticlesList.isEmpty()) {
            for (ApHotArticles apHotArticles : hotArticlesList) {
                apHotArticleService.deleteHotData(apHotArticles);
            }
        }
    }

}
