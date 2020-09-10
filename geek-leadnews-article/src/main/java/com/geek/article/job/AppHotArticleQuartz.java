//package com.geek.article.job;
//
//import com.geek.article.service.IApHotArticleService;
//import com.geek.common.quartz.AbstractJob;
//import lombok.extern.log4j.Log4j2;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//@Log4j2
//public class AppHotArticleQuartz extends AbstractJob {
//
//    @Autowired
//    private IApHotArticleService apHotArticleService;
//
//    @Override
//    public String[] triggerCron() {
////        return new String[]{"0 0/5 0 * * ?"};
//        return new String[]{"0 0/1 * * * ?"};
//    }
//
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        long currentTimeMillis = System.currentTimeMillis();
//        log.info("开始定时计算分值。");
//        apHotArticleService.computeHotArticle();
//        log.info("计算分值完成，耗时：{}", System.currentTimeMillis() - currentTimeMillis);
//    }
//
//    @Override
//    public String descTrigger() {
//        return "每天 00:05 分执行一次。";
//    }
//
//}
