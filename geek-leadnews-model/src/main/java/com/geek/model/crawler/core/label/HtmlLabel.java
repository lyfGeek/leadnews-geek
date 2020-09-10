package com.geek.model.crawler.core.label;

import lombok.Data;

import java.io.Serializable;

@Data
public class HtmlLabel implements Serializable {

    /**
     * 解析的数据类型。
     */
    private String type;
    /**
     * 标签内容。
     */
    private String value;
    /**
     * 设置样式。
     */
    private String style;

}
