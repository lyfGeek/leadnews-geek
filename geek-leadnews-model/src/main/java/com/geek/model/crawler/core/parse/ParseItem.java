package com.geek.model.crawler.core.parse;

import lombok.Data;

import java.io.Serializable;

/**
 * 解析封装对象。
 */
@Data
public abstract class ParseItem implements Serializable {

    /**
     * 处理类型。有正向、反向两种。
     * FORWARD ~ 正向。
     * REVERSE ~ 反向。
     */
    private String handleType = null;
    /**
     * 文档抓取类型。
     */
    private String documentType = null;
    /**
     * 渠道名称。
     */
    private String channelName;

    /**
     * 获取初始的 URL。
     *
     * @return
     */
    public abstract String getInitialUrl();

    /**
     * 获取需要处理的内容。
     *
     * @return
     */
    public abstract String getParserContent();

}
