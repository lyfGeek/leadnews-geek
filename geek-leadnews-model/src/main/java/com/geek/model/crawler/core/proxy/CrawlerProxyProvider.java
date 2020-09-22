package com.geek.model.crawler.core.proxy;

import com.geek.model.crawler.core.callback.IProxyProviderCallBack;

import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 代理 IP 的提供者。
 */
public class CrawlerProxyProvider {

    /**
     * 读写锁特点。
     * 读读共享。
     * 写写互斥。
     * 读写互斥。
     */
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    // 获取读锁。
    private Lock readLock = lock.readLock();
    // 获取写锁。
    private Lock writeLock = lock.writeLock();
    /**
     * 随机数生成器，用以随机获取代理 IP。
     */
    private Random random = new Random();
    /**
     * 是否启动代理 IP。
     */
    private boolean isUsedProxyIp = true;
    /**
     * 动态代理 IP 自动更新阈值。
     */
    private int proxyIpUpdateThreshold = 10;
    /**
     * 代理 Ip 池。
     */
    private List<CrawlerProxy> crawlerProxyList = null;
    /**
     * ip 池回调。
     */
    private IProxyProviderCallBack proxyProviderCallBack;

    public CrawlerProxyProvider() {
    }

    public CrawlerProxyProvider(List<CrawlerProxy> crawlerProxyList) {
        this.crawlerProxyList = crawlerProxyList;
    }

    /**
     * 随机获取一个代理 IP。
     * 保证每次请求使用的 IP 都不一样。
     *
     * @return
     */
    public CrawlerProxy getRandomProxy() {
        CrawlerProxy crawlerProxy = null;
        readLock.lock();
        try {
            if (isUsedProxyIp && null != crawlerProxyList && !crawlerProxyList.isEmpty()) {
                int randomIndex = random.nextInt(crawlerProxyList.size());
                crawlerProxy = crawlerProxyList.get(randomIndex);
            }
        } finally {
            readLock.unlock();
        }
        return crawlerProxy;
    }

    public void updateProxy() {
        // 不使用代理 IP，则不进行更新。
        if (isUsedProxyIp && null != proxyProviderCallBack) {
            writeLock.lock();
            try {
                crawlerProxyList = proxyProviderCallBack.getProxyList();
            } finally {
                writeLock.unlock();
            }
        }
    }

    /**
     * 设置代理 IP 不可用。
     *
     * @param proxy
     */
    public void unavailable(CrawlerProxy proxy) {
        if (isUsedProxyIp) {
            writeLock.lock();
            crawlerProxyList.remove(proxy);// 从 ip 池中移除。
            writeLock.unlock();
//            proxyProviderCallBack.unvailable(proxy);
            if (crawlerProxyList.size() <= proxyIpUpdateThreshold) {
                updateProxy();
            }
        }
    }

    public List<CrawlerProxy> getCrawlerProxyList() {
        return crawlerProxyList;
    }

    public void setCrawlerProxyList(List<CrawlerProxy> crawlerProxyList) {
        this.crawlerProxyList = crawlerProxyList;
    }

    public boolean isUsedProxyIp() {
        return isUsedProxyIp;
    }

    public void setUsedProxyIp(boolean usedProxyIp) {
        isUsedProxyIp = usedProxyIp;
    }

    public IProxyProviderCallBack getProxyProviderCallBack() {
        return proxyProviderCallBack;
    }

    public void setProxyProviderCallBack(IProxyProviderCallBack proxyProviderCallBack) {
        this.proxyProviderCallBack = proxyProviderCallBack;
    }

}
