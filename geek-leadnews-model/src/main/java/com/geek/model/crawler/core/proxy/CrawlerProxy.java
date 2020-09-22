package com.geek.model.crawler.core.proxy;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 代理 IP 实体类。
 */
@Data
@AllArgsConstructor
public class CrawlerProxy implements Serializable {

    private String host;
    private Integer port;

    /**
     * 获取代理信息。
     *
     * @return
     */
    public String getProxyInfo() {
        return this.host + ":" + port;
    }

}
