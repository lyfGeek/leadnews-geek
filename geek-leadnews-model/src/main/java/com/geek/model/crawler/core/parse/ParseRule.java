package com.geek.model.crawler.core.parse;

import com.geek.model.crawler.enums.CrawlerEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 抓取内容封装。
 */
@Data
public class ParseRule implements Serializable {

    /**
     * 映射字段。
     */
    private String field;
    /**
     * URL 校验规则。
     */
    private String urlValidateRegular;
    /**
     * 解析规则类型。
     */
    private CrawlerEnum.ParseRuleType parseRuleType;
    /**
     * 规则。
     */
    private String rule;
    /**
     * 抓取内容列表。
     */
    private List<String> parseContentList;

    public ParseRule() {
    }

    /**
     * 构造方法。
     *
     * @param field
     * @param parseRuleType
     * @param rule
     */
    public ParseRule(String field, CrawlerEnum.ParseRuleType parseRuleType, String rule) {
        this.field = field;
        this.parseRuleType = parseRuleType;
        this.rule = rule;
    }

    /**
     * 检查是否有效，如果内容为空则判断该类为空。
     *
     * @return
     */
    public boolean isAvailability() {
        boolean isAvailability = false;
        if (null != parseContentList && !parseContentList.isEmpty()) {
            isAvailability = true;
        }
        return isAvailability;
    }

    /**
     * 获取合并后的内容。
     *
     * @return
     */
    public String getMergeContent() {
        StringBuilder stringBuilder = new StringBuilder();
        if (null != parseContentList && !parseContentList.isEmpty()) {
            for (String str : parseContentList) {
                stringBuilder.append(str);
            }
        }
        return stringBuilder.toString();
    }

}
