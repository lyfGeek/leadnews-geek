package com.geek.crawler.service;

import com.geek.model.crawler.core.proxy.CrawlerProxy;
import com.geek.model.crawler.pojos.ClIpPool;

import java.util.List;

public interface ICrawlerIpPoolService {

    /**
     * 保存。
     *
     * @param clIpPool
     */
    void saveCrawlerIpPool(ClIpPool clIpPool);

    /**
     * 检查 ip 是否存在。
     *
     * @return
     */
    boolean checkExist(String host, int port);

    /**
     * 更新方法。
     *
     * @param clIpPool
     */
    void updateCrawlerIpPool(ClIpPool clIpPool);

    /**
     * 查询所有数据。
     *
     * @param clIpPool
     * @return
     */
    List<ClIpPool> queryList(ClIpPool clIpPool);

    /**
     * 查询可用的 ip 列表。
     *
     * @param clIpPool
     * @return
     */
    List<ClIpPool> queryAvailableList(ClIpPool clIpPool);

    /**
     * 删除。
     *
     * @param clIpPool
     */
    void delete(ClIpPool clIpPool);

    /**
     * 设置某个 ip 不可用。
     *
     * @param proxy
     * @param errorMsg
     */
    void unAvailableProxy(CrawlerProxy proxy, String errorMsg);

}
