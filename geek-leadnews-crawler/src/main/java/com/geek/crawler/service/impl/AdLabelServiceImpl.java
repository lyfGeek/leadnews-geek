package com.geek.crawler.service.impl;

import com.geek.common.common.util.HMStringUtils;
import com.geek.crawler.service.IAdLabelService;
import com.geek.model.admin.pojos.AdChannelLabel;
import com.geek.model.admin.pojos.AdLabel;
import com.geek.model.mappers.admin.IAdChannelLabelMapper;
import com.geek.model.mappers.admin.IAdLabelMapper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class AdLabelServiceImpl implements IAdLabelService {

    @Autowired
    private IAdLabelMapper adLabelMapper;

    @Autowired
    private IAdChannelLabelMapper adChannelLabelMapper;

    /**
     * @param labels 从页面爬取的标签。多个的时候，以逗号分隔。
     * @return 标签 id。多个以逗号分隔。
     */
    @Override
    public String getLabelIds(String labels) {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("获取 channel 信息。标签：labels：{}", labels);
        List<AdLabel> adLabelList = new ArrayList<>();
        if (StringUtils.isNotEmpty(labels)) {
            // 转换成小写。
            labels = labels.toLowerCase();
            List<String> tmpLabels = Arrays.asList(labels.split(","));
            tmpLabels = new ArrayList<>(tmpLabels);
            adLabelList = adLabelMapper.queryAdLabelByLabels(tmpLabels);
            if (null != adLabelList && !adLabelList.isEmpty()) {
                adLabelList = addLabelList(tmpLabels, adLabelList);// 去重。
            } else {// 数据库中为空，保存。
                adLabelList = addLabelList(tmpLabels);
            }
        }
        List<String> labelList = adLabelList.stream().map(label -> HMStringUtils.toString(label.getId())).collect(Collectors.toList());
        String resultStr = HMStringUtils.listToStr(labelList, ",");// 集合转字符串。
        log.info("获取 channel 信息完成。标签：{}，labelIds：{}，耗时：{}", labels, resultStr, System.currentTimeMillis() - currentTimeMillis);
        return resultStr;
    }

    /**
     * 去重过滤保存。
     *
     * @param tmpLabels
     * @param adLabelList 数据库中的数据。
     * @return
     */
    private List<AdLabel> addLabelList(List<String> tmpLabels, List<AdLabel> adLabelList) {
        if (tmpLabels != null && !tmpLabels.isEmpty()) {
            for (AdLabel adLabel : adLabelList) {
                for (int i = 0; i < tmpLabels.size(); i++) {
                    // list 去除数据库中已有数据。
                    if (tmpLabels.get(i).contains(adLabel.getName())) {
                        tmpLabels.remove(i);
                    }
                }
            }
        }
        // 将不重复的数据加入到数据库的数据列表并返回。
        if (tmpLabels != null && !tmpLabels.isEmpty()) {
            adLabelList.addAll(addLabelList(tmpLabels));
        }
        return adLabelList;
    }

    /**
     * 保存。
     *
     * @param tmpLabels
     * @return
     */
    private List<AdLabel> addLabelList(List<String> tmpLabels) {
        List<AdLabel> adLabelList = new ArrayList<>();
        for (String label : tmpLabels) {
            adLabelList.add(addLabel(label));
        }
        return adLabelList;
    }

    /**
     * 保存操作。
     *
     * @param label
     * @return
     */
    private AdLabel addLabel(String label) {
        AdLabel adLabel = new AdLabel();
        adLabel.setName(label);
        adLabel.setType(true);
        adLabel.setCreatedTime(new Date());
        adLabelMapper.insert(adLabel);
        return adLabel;
    }

    /**
     * @param labelIds 标签 id。多个以逗号分隔。
     * @return 频道 id。找不到频道，默认给 0 ~ 未分类。
     */
    @Override
    public Integer getAdChannelByLabelIds(String labelIds) {
        Integer channelId = 0;
        try {
            channelId = getSecurityAdChannelByLabelIds(labelIds);
        } catch (Exception e) {
            log.error("获取 channel 信息失败。errorMsg：{}", e.getMessage());
        }
        return channelId;
    }

    private Integer getSecurityAdChannelByLabelIds(String labelIds) {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("获取 channel 信息，标签 ids：{}", labelIds);
        Integer channelId = 0;
        if (StringUtils.isNotEmpty(labelIds)) {// 1,2,3
            List<String> labelList = Arrays.asList(labelIds.split(","));
            List<AdLabel> adLabelList = adLabelMapper.queryAdLabelByLabelIds(labelList);
            if (null != adLabelList && !adLabelList.isEmpty()) {
                channelId = getAdChannelIdByLabelId(adLabelList.get(0).getId());
            }
            channelId = channelId == null ? 0 : channelId;
        }

        log.info("获取 channel 信息完成。标签：{}，channelId：{}，耗时：{}", labelIds, channelId, System.currentTimeMillis() - currentTimeMillis);
        return channelId;
    }

    private Integer getAdChannelIdByLabelId(Integer labelId) {
        Integer channelId = 0;
        AdChannelLabel adChannelLabel = adChannelLabelMapper.selectByLabelId(labelId);
        if (adChannelLabel != null) {
            channelId = adChannelLabel.getChannelId();
        }
        return channelId;
    }

}
