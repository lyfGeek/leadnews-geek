package com.geek.crawler.service;

public interface IAdLabelService {

    /**
     * @param labels 从页面爬取的标签。多个的时候，以逗号分隔。
     * @return 标签 id。多个以逗号分隔。
     */
    String getLabelIds(String labels);

    /**
     * @param labelIds 标签 id。多个以逗号分隔。
     * @return 频道 id。找不到频道，默认给 0 ~ 未分类。
     */
    Integer getAdChannelByLabelIds(String labelIds);

}
