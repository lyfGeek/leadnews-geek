package com.geek.crawler.service.impl;

import com.geek.crawler.service.ICrawlerIpPoolService;
import com.geek.model.crawler.core.proxy.CrawlerProxy;
import com.geek.model.crawler.pojos.ClIpPool;
import com.geek.model.mappers.crawerls.IClIpPoolMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class CrawlerIpPoolServiceImpl implements ICrawlerIpPoolService {

    @Autowired
    private IClIpPoolMapper clIpPoolMapper;

    @Override
    public void saveCrawlerIpPool(ClIpPool clIpPool) {
        clIpPoolMapper.insertSelective(clIpPool);
    }

    @Override
    public boolean checkExist(String host, int port) {
        ClIpPool clIpPool = new ClIpPool();
        clIpPool.setIp(host);
        clIpPool.setPort(port);
        List<ClIpPool> clIpPools = clIpPoolMapper.selectList(clIpPool);
        return null != clIpPools && !clIpPools.isEmpty();
    }

    @Override
    public void updateCrawlerIpPool(ClIpPool clIpPool) {
        clIpPoolMapper.updateByPrimaryKey(clIpPool);
    }

    @Override
    public List<ClIpPool> queryList(ClIpPool clIpPool) {
        return clIpPoolMapper.selectList(clIpPool);
    }

    @Override
    public List<ClIpPool> queryAvailabelList(ClIpPool clIpPool) {
        return clIpPoolMapper.selectAvailableList(clIpPool);
    }

    @Override
    public void delete(ClIpPool clIpPool) {
        clIpPoolMapper.deleteByPrimaryKey(clIpPool.getId());
    }

    @Override
    public void unAvailableProxy(CrawlerProxy proxy, String errorMsg) {
        ClIpPool clIpPool = new ClIpPool();
        clIpPool.setIp(proxy.getHost());
        clIpPool.setPort(proxy.getPort());
        clIpPool.setEnable(true);
        List<ClIpPool> clIpPools = clIpPoolMapper.selectList(clIpPool);
        if (null != clIpPools && !clIpPools.isEmpty()) {
            for (ClIpPool ipPool : clIpPools) {
                ipPool.setEnable(false);
                ipPool.setError(errorMsg);
                clIpPoolMapper.updateByPrimaryKey(ipPool);
            }
        }
    }

}
