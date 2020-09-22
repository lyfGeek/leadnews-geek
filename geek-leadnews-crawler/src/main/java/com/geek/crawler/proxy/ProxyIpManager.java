//package com.geek.crawler.proxy;
//
//import com.geek.common.common.util.HMStringUtils;
//import com.geek.crawler.service.ICrawlerIpPoolService;
//import com.geek.crawler.utils.ProxyIpUtils;
//import com.geek.crawler.utils.SeleniumClient;
//import com.geek.model.crawler.core.cookie.CrawlerHtml;
//import com.geek.model.crawler.core.proxy.CrawlerProxyProvider;
//import com.geek.model.crawler.core.proxy.ProxyValidate;
//import com.geek.model.crawler.pojos.ClIpPool;
//import lombok.extern.log4j.Log4j2;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
///**
// * 动态代理 ip 管理类。
// */
//@Component
//@Log4j2
//public class ProxyIpManager {
//
//    /**
//     * 抓取 IP 的正则表达式，预编译模式。
//     */
//    Pattern proxyIpPattern = Pattern.compile("(\\d+\\.\\d+\\.\\d+\\.\\d+)\\:(\\d+)");
//    @Autowired
//    private ICrawlerIpPoolService crawlerIpPoolService;
//    @Autowired
//    private SeleniumClient seleniumClient;
//    @Autowired
//    private CrawlerProxyProvider crawlerProxyProvider;
//    @Value("${proxy.get.url}")
//    private String proxyGetUrl;
//
//    /**
//     * 检验动态代理 ip。
//     */
//    public void validateProxyIp() {
//        List<ClIpPool> clIpPoolList = crawlerIpPoolService.queryList(new ClIpPool());
//        if (null != clIpPoolList && !clIpPoolList.isEmpty()) {
//            for (ClIpPool clIpPool : clIpPoolList) {
//                Boolean ipPoolEnable = clIpPool.getEnable();
//                // 检验状态是否可用。
//                validateProxyIp(clIpPool);
//                // 如果原始状态以及当前状态都不可用，则判断是否废除的代理，删除。
//                if (!ipPoolEnable && !clIpPool.getEnable()) {
//                    // 删除代理。
//                    log.info("删除代理ip,ip：{}，port：{}", clIpPool.getIp(), clIpPool.getPort());
//                    crawlerIpPoolService.delete(clIpPool);
//                } else {
//                    crawlerIpPoolService.updateCrawlerIpPool(clIpPool);
//                    log.info("更新代理ip,ip：{}，port：{}", clIpPool.getIp(), clIpPool.getPort());
//                }
//            }
//        }
//    }
//
//    /**
//     * 检验 ip 是否可用。
//     *
//     * @param clIpPool
//     */
//    private void validateProxyIp(ClIpPool clIpPool) {
//        clIpPool.setEnable(false);
//        ProxyValidate proxyValidate = new ProxyValidate(clIpPool.getIp(), clIpPool.getPort());
//        try {
//            ProxyIpUtils.validateProxyIp(proxyValidate);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//        if (proxyValidate.getReturnCode() == 200) {
//            clIpPool.setEnable(true);
//        }
//        clIpPool.setCode(proxyValidate.getReturnCode());
//        clIpPool.setDuration(proxyValidate.getDuration());
//        clIpPool.setError(HMStringUtils.getFixedLengthStr(proxyValidate.getError(), 70));
//
//    }
//
//    /**
//     * 更新动态代理 ip。
//     */
//    public void updateProxyIp() {
//        // 在网上抓取免费的 ip。
//        List<ClIpPool> clIpPoolList = getGrabClIpPoolList();
//        if (null != clIpPoolList && !clIpPoolList.isEmpty()) {
//            for (ClIpPool clIpPool : clIpPoolList) {
//                validateProxyIp(clIpPool);
//                if (clIpPool.getEnable()) {
//                    boolean exist = crawlerIpPoolService.checkExist(clIpPool.getIp(), clIpPool.getPort());
//                    if (!exist) {
//                        crawlerIpPoolService.saveCrawlerIpPool(clIpPool);
//                        log.info("插入代理 ip，ip：{}，port：{}", clIpPool.getIp(), clIpPool.getPort());
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 获取抓取的动态代理 ip。
//     *
//     * @return
//     */
//    private List<ClIpPool> getGrabClIpPoolList() {
//        List<ClIpPool> clIpPoolList = new ArrayList<>();
//        // 使用 seleniumUtils 的方式获取代理 ip 数据。
//        CrawlerHtml crawlerHtml = seleniumClient.getCrawlerHtml(proxyGetUrl, crawlerProxyProvider.getRandomProxy(), "yd_cookie");
//        if (null != crawlerHtml && StringUtils.isNotEmpty(crawlerHtml.getHtml())) {
//            Matcher matcher = proxyIpPattern.matcher(crawlerHtml.getHtml());
//            while (matcher.find()) {
//                String host = matcher.group(1);
//                String port = matcher.group(2);
//                ClIpPool clIpPool = new ClIpPool();
//                clIpPool.setIp(host);
//                clIpPool.setPort(Integer.parseInt(port));
//                clIpPool.setCreatedTime(new Date());
//                clIpPool.setSupplier("89免费代理");
//                clIpPoolList.add(clIpPool);
//            }
//        }
//        return clIpPoolList;
//    }
//
//}
