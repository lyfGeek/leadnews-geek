package com.geek.crawler.job;

import com.geek.common.quartz.AbstractJob;
import com.geek.crawler.proxy.ProxyIpManager;
import com.geek.model.crawler.core.proxy.CrawlerProxyProvider;
import lombok.extern.log4j.Log4j2;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
@Log4j2
public class ProxyIpValidateQuartz extends AbstractJob {

    @Autowired
    private ProxyIpManager proxyIpManager;
    @Autowired
    private CrawlerProxyProvider crawlerProxyProvider;

    @Override
    public String[] triggerCron() {
        return new String[]{"0 0/10 * * * ?"};
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("开始检验代理 ip。");
//        proxyIpManager.validateProxyIp();
//        crawlerProxyProvider.updateProxy();
        log.info("检验代理 ip 完成，耗时：{}", System.currentTimeMillis() - currentTimeMillis);
    }
}
