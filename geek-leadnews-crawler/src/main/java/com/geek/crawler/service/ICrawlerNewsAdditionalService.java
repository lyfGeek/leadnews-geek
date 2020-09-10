package com.geek.crawler.service;

import com.geek.model.crawler.core.parse.ParseItem;
import com.geek.model.crawler.pojos.ClNewsAdditional;

import java.util.Date;
import java.util.List;

public interface ICrawlerNewsAdditionalService {

    /**
     * 保存。
     *
     * @param clNewsAdditional
     */
    void saveAdditional(ClNewsAdditional clNewsAdditional);

    /**
     * 根据当前需要更新的时间查询列表。
     *
     * @param currentDate
     * @return
     */
    List<ClNewsAdditional> queryListByNeedUpdate(Date currentDate);

    /**
     * 根据条件查询。
     *
     * @param clNewsAdditional
     * @return
     */
    List<ClNewsAdditional> queryList(ClNewsAdditional clNewsAdditional);

    /**
     * 检查是否存在。
     *
     * @param url
     * @return
     */
    boolean checkExist(String url);

    /**
     * 根据 url 获取图片附加信息。
     *
     * @param url
     * @return
     */
    ClNewsAdditional getAdditionalByUrl(String url);

    /**
     * 是否是已存在的 url。
     *
     * @param url
     * @return
     */
    boolean isExistUrl(String url);

    /**
     * 更新。
     *
     * @param clNewsAdditional
     */
    void updateAdditional(ClNewsAdditional clNewsAdditional);

    /**
     * 转换数据为 parseItem。
     *
     * @param additionalList
     * @return
     */
    List<ParseItem> toParseItem(List<ClNewsAdditional> additionalList);

    /**
     * 查询增量的统计数据。
     *
     * @param currentDate
     * @return
     */
    List<ParseItem> queryIncrementParseItem(Date currentDate);

}
