//package com.geek.crawler.process.scheduler;
//
//import com.geek.crawler.helper.CrawlerHelper;
//import com.geek.crawler.process.IProcessFlow;
//import com.geek.crawler.process.entity.ProcessFlowData;
//import com.geek.crawler.service.ICrawlerNewsAdditionalService;
//import com.geek.model.crawler.enums.CrawlerEnum;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import redis.clients.jedis.JedisPool;
//import us.codecraft.webmagic.Request;
//import us.codecraft.webmagic.Task;
//import us.codecraft.webmagic.scheduler.RedisScheduler;
//
//@Log4j2
//public class DbAndRedisScheduler extends RedisScheduler implements IProcessFlow {
//
//    @Autowired
//    private ICrawlerNewsAdditionalService crawlerNewsAdditionalService;
//
//    @Autowired
//    private CrawlerHelper crawlerHelper;
//
//    public DbAndRedisScheduler(String host) {
//        super(host);
//    }
//
//    public DbAndRedisScheduler(JedisPool pool) {
//        super(pool);
//    }
//
//    @Override
//    public boolean isDuplicate(Request request, Task task) {
//        String handleType = crawlerHelper.gethandleType(request);
//        boolean isExist = false;
//        // 只有正向爬取的时候才会排重。
//        if (CrawlerEnum.handleType.FORWARD.name().equals(handleType)) {
//            log.info("url排重开始，url：{}，handleType：{}", request.getUrl(), handleType);
//            // Redis 排重。
//            isExist = super.isDuplicate(request, task);
//            // 数据库进行排重。
//            if (!isExist) {
//                isExist = crawlerNewsAdditionalService.isExistUrl(request.getUrl());
//            }
//            log.info("url排重结束，url：{}，handleType：{}，isExist：{}", request.getUrl(), handleType, isExist);
//        } else {
//            log.info("反向爬取，不进行 url 排重。");
//        }
//        return isExist;
//    }
//
//    @Override
//    public void handle(ProcessFlowData processFlowData) {
//
//    }
//
//    @Override
//    public CrawlerEnum.ComponentType getComponentType() {
//        return CrawlerEnum.ComponentType.SCHEDULER;
//    }
//
//    @Override
//    public int getPriority() {
//        return 123;
//    }
//
//}
