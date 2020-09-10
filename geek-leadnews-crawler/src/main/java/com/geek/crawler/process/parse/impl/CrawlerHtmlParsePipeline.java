package com.geek.crawler.process.parse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geek.common.common.util.HMStringUtils;
import com.geek.common.kafka.KafkaSender;
import com.geek.common.kafka.messages.SubmitArticleAuthMessage;
import com.geek.crawler.process.parse.AbstractHtmlParsePipeline;
import com.geek.crawler.process.thread.CrawlerThreadPool;
import com.geek.crawler.service.IAdLabelService;
import com.geek.crawler.service.ICrawlerNewsAdditionalService;
import com.geek.crawler.service.ICrawlerNewsCommentService;
import com.geek.crawler.service.ICrawlerNewsService;
import com.geek.crawler.utils.DateUtils;
import com.geek.crawler.utils.HtmlParser;
import com.geek.model.crawler.core.label.HtmlLabel;
import com.geek.model.crawler.core.parse.ZipUtils;
import com.geek.model.crawler.core.parse.impl.CrawlerParseItem;
import com.geek.model.crawler.enums.CrawlerEnum;
import com.geek.model.crawler.pojos.ClNews;
import com.geek.model.crawler.pojos.ClNewsAdditional;
import com.geek.model.crawler.pojos.ClNewsComment;
import com.geek.model.mess.admin.SubmitArticleAuto;
import com.geek.utils.common.ReflectUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
// @PropertySource("classpath:crawler.properties")
@Log4j2
public class CrawlerHtmlParsePipeline extends AbstractHtmlParsePipeline<CrawlerParseItem> {

    //     @Value("${csdn.comment.url}")
//     private String csdnCommentUrl;
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("crawler");
    private static final String csdn_comment_url = resourceBundle.getString("csdn.comment.url");
    @Autowired
    private KafkaSender kafkaSender;
    @Autowired
    private ICrawlerNewsCommentService crawlerNewsCommentService;
    @Value("${crawler.nextupdatehours}")
    private String nextUpdateHours;
    @Autowired
    private ICrawlerNewsAdditionalService crawlerNewsAdditionalService;
    @Autowired
    private ICrawlerNewsService crawlerNewsService;
    @Autowired
    private IAdLabelService adLabelService;

    /**
     * 数据处理的入口。
     *
     * @param parseItem
     */
    @Override
    public void handleHtmlData(CrawlerParseItem parseItem) {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("将数据加入线程池进行执行，url：{}，handelType：{}", parseItem.getUrl(), parseItem.getHandelType());
        CrawlerThreadPool.submit(() -> {
            // 正向抓取
            if (CrawlerEnum.HandelType.FORWARD.name().equals(parseItem.getHandelType())) {
                log.info("开始处理消息，url：{}，handelType：{}", parseItem.getUrl(), parseItem.getHandelType());
                addParseItemMessage(parseItem);
            } else if (CrawlerEnum.HandelType.REVERSE.name().equals(parseItem.getHandelType())) {
                updateAdditional(parseItem);
            }

            log.info("处理文章数据结束，url：{}，handelType：{}，耗时：{}", parseItem.getUrl(), parseItem.getHandelType(), System.currentTimeMillis() - currentTimeMillis);
        });
    }

    /**
     * 逆向更新文章附加信息。
     *
     * @param parseItem
     */
    private void updateAdditional(CrawlerParseItem parseItem) {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("开始更新文章附加数据");
        if (null != parseItem) {
            ClNewsAdditional clNewsAdditional = crawlerNewsAdditionalService.getAdditionalByUrl(parseItem.getUrl());
            if (null != clNewsAdditional) {
                clNewsAdditional.setNewsId(null);
                clNewsAdditional.setUrl(null);
                clNewsAdditional.setReadCount(parseItem.getReadCount());
                clNewsAdditional.setComment(parseItem.getCommentCount());
                clNewsAdditional.setLikes(parseItem.getLikes());
                int nextUpdateHours = getNextUpdateHours(clNewsAdditional.getUpdateNum());
                clNewsAdditional.setNextUpdateTime(DateUtils.addHours(new Date(), nextUpdateHours));
                clNewsAdditional.setUpdateNum(clNewsAdditional.getUpdateNum() + 1);
                crawlerNewsAdditionalService.updateAdditional(clNewsAdditional);
            }
        }
        log.info("更新文章附加数据完成，耗时：{}", System.currentTimeMillis() - currentTimeMillis);
    }

    /**
     * 正向保存数据发送消息。
     *
     * @param parseItem
     */
    private void addParseItemMessage(CrawlerParseItem parseItem) {
        long currentTimeMillis = System.currentTimeMillis();
        String url = null;
        String handelTyep = null;
        if (null != parseItem) {
            url = parseItem.getUrl();
            handelTyep = parseItem.getHandelType();
            log.info("开始添加数据，url：{}，handelType：{}", url, handelTyep);
            // 添加文章数据。
            ClNews clNews = addClNewsData(parseItem);
            if (null != clNews) {
                // 添加附加信息。
                addAdditionalData(parseItem, clNews);
                // 添加评论数据。只有评论数大于 0 才添加数据。
                if (null != parseItem && null != parseItem.getCommentCount() && parseItem.getCommentCount() > 0) {
                    addCommentData(parseItem, clNews);
                }
                // 发送消息审核文章。
                sendSubmitArticleAutoMessage(clNews.getId());
            }
        }
        log.info("添加数据完成，url：{}，handelType：{}，耗时：{}", url, handelTyep, System.currentTimeMillis() - currentTimeMillis);
    }

    /**
     * 发送消息，审核文章。
     *
     * @param id
     */
    private void sendSubmitArticleAutoMessage(Integer id) {
        log.info("开始发送自动审核文章消息，id：{}", id);
        SubmitArticleAuto submitArticleAuto = new SubmitArticleAuto();
        submitArticleAuto.setArticleId(id);
        submitArticleAuto.setType(SubmitArticleAuto.ArticleType.CRAWLER);
        SubmitArticleAuthMessage authMessage = new SubmitArticleAuthMessage();
        authMessage.setData(submitArticleAuto);
        kafkaSender.sendSubmitArticleAuthMessage(authMessage);
        log.info("发送自动审核消息完成，id：{}", id);
    }

    /**
     * 添加评论数据。
     *
     * @param parseItem
     * @param clNews
     */
    private void addCommentData(CrawlerParseItem parseItem, ClNews clNews) {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("开始获取文章评论数据。");
        List<ClNewsComment> commentList = getCommentData(parseItem);
        if (null != commentList && !commentList.isEmpty()) {
            for (ClNewsComment clNewsComment : commentList) {
                clNewsComment.setNewsId(clNews.getId());
                crawlerNewsCommentService.saveClNewsComment(clNewsComment);
            }
        }
        log.info("获取文章评论数据完成，耗时：{}", System.currentTimeMillis() - currentTimeMillis);
    }

    /**
     * 获取评论列表。
     *
     * @param parseItem
     * @return
     */
    private List<ClNewsComment> getCommentData(CrawlerParseItem parseItem) {
        // 构建评论的 url。
        String buildCommentUrl = buildCommentUrl(parseItem);
        // 发送请求，获取数据。
        String jsonData = getOriginalRequestJsonData(buildCommentUrl, null);
        // 解析获取的 json 数据。
        List<ClNewsComment> commentList = analysisCommentJsonData(jsonData);
        return commentList;
    }

    /**
     * 解析评论数据。
     *
     * @param jsonData
     * @return
     */
    private List<ClNewsComment> analysisCommentJsonData(String jsonData) {
        if (StringUtils.isEmpty(jsonData)) {
            return null;
        }
        List<ClNewsComment> commentList = new ArrayList<>();
        JSONObject jsonObject = JSON.parseObject(jsonData);
        Map<String, Object> map = jsonObject.getObject("data", Map.class);
        JSONArray jsonArray = (JSONArray) map.get("list");
        if (null != jsonArray) {
            List<Map> dataInfoList = jsonArray.toJavaList(Map.class);
            for (Map<String, Object> dataInfo : dataInfoList) {
                JSONObject infoObject = (JSONObject) dataInfo.get("info");
                Map<String, Object> infoMap = infoObject.toJavaObject(Map.class);
                ClNewsComment comment = new ClNewsComment();
                comment.setContent(HMStringUtils.toString(infoMap.get("Content")));
                comment.setUsername(HMStringUtils.toString(infoMap.get("UserName")));
                Date date = DateUtils.stringToDate(HMStringUtils.toString(infoMap.get("PostTime")));
                comment.setCommentDate(date);
                comment.setCreatedDate(new Date());
                commentList.add(comment);
            }
        }
        return commentList;
    }

    /**
     * 生成评论访问链接。
     *
     * @param parseItem
     * @return
     */
    private String buildCommentUrl(CrawlerParseItem parseItem) {
        String buildCommentUrl = csdn_comment_url;
        Map<String, Object> map = ReflectUtils.beanToMap(parseItem);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            String buildKey = "${" + key + "}";
            Object value = entry.getValue();
            if (null != value) {
                String strValue = value.toString();
                buildCommentUrl = buildCommentUrl.replace(buildKey, strValue);
            }
        }
        return buildCommentUrl;
    }

    /**
     * 保存附加信息。
     *
     * @param parseItem
     * @param clNews
     */
    private void addAdditionalData(CrawlerParseItem parseItem, ClNews clNews) {
        long currentTimeMillis = System.currentTimeMillis();
        log.info("开始处理文章附件信息数据。");
        if (null != parseItem && null != clNews) {
            ClNewsAdditional clNewsAdditional = toClNewsAdditional(parseItem, clNews);
            crawlerNewsAdditionalService.saveAdditional(clNewsAdditional);
        }
        log.info("文章附加数据处理完成，耗时：{}", System.currentTimeMillis() - currentTimeMillis);
    }

    /**
     * 转换数据 additional。
     *
     * @param parseItem
     * @param clNews
     * @return
     */
    private ClNewsAdditional toClNewsAdditional(CrawlerParseItem parseItem, ClNews clNews) {
        ClNewsAdditional clNewsAdditional = null;
        if (null != parseItem) {
            clNewsAdditional = new ClNewsAdditional();
            clNewsAdditional.setNewsId(clNews.getId());// 文章 id。
            clNewsAdditional.setReadCount(parseItem.getReadCount());// 阅读数。
            clNewsAdditional.setComment(parseItem.getCommentCount());// 回复数。
            clNewsAdditional.setLikes(parseItem.getLikes());// 点赞。
            clNewsAdditional.setUrl(parseItem.getUrl());// url。
            clNewsAdditional.setUpdatedTime(new Date());
            clNewsAdditional.setCreatedTime(new Date());
            clNewsAdditional.setUpdateNum(0);
            // 设置下次更新时间。
            int nextUpdateHour = getNextUpdateHours(clNewsAdditional.getUpdateNum());
            // 设置下次更新时间。
            clNewsAdditional.setNextUpdateTime(DateUtils.addHours(new Date(), nextUpdateHour));
        }
        return clNewsAdditional;
    }

    /**
     * 计算更新次数。
     *
     * @param updateNum
     * @return
     */
    private int getNextUpdateHours(Integer updateNum) {
        if (StringUtils.isNotEmpty(nextUpdateHours)) {
            String[] updateArray = nextUpdateHours.split(",");
            return Integer.parseInt(updateArray[updateNum]);
        } else {
            return 2 << updateNum;
        }
    }

    /**
     * 保存文章信息。
     *
     * @param parseItem
     * @return
     */
    private ClNews addClNewsData(CrawlerParseItem parseItem) {
        log.info("开始添加文章");
        ClNews clNews = null;
        if (null != parseItem) {
            // 内容 content  html文本--》固定的格式。
            HtmlParser htmlParser = HtmlParser.getHtmlParser(getParseExpression(), getDefHtmlStyleMap());
            List<HtmlLabel> htmlLabels = htmlParser.parseHtml(parseItem.getContent());
            int type = getDocType(htmlLabels);
            parseItem.setDocType(type);// 图文类型。0 ~ 无图。1 ~单图。2 多图。

            String jsonStr = JSON.toJSONString(htmlLabels);
            parseItem.setCompressContent(ZipUtils.gzip(jsonStr));

            // 添加文章。
            ClNewsAdditional additionalByUrl = crawlerNewsAdditionalService.getAdditionalByUrl(parseItem.getUrl());
            if (null == additionalByUrl) {
                clNews = toClNews(parseItem);
                long currentTimeMillis = System.currentTimeMillis();
                log.info("开始插入新的文章。");
                crawlerNewsService.saveNews(clNews);
                log.info("插入新的文章完成，耗时：{}", System.currentTimeMillis() - currentTimeMillis);
            } else {
                log.info("文章 url 已经存在，不能重复添加，url：{}", additionalByUrl.getUrl());
            }
        }
        log.info("添加文章内容完成。");
        return clNews;
    }

    private ClNews toClNews(CrawlerParseItem parseItem) {
        ClNews clNews = new ClNews();
        clNews.setName(parseItem.getAuthor());
        clNews.setLabels(parseItem.getLabels());
        clNews.setContent(parseItem.getCompressContent());
        clNews.setLabelIds(adLabelService.getLabelIds(parseItem.getLabels()));
        Integer channelId = adLabelService.getAdChannelByLabelIds(clNews.getLabelIds());
        clNews.setChannelId(channelId);
        clNews.setTitle(parseItem.getTitle());
        clNews.setType(parseItem.getDocType());
        clNews.setStatus((byte) 1);
        clNews.setCreatedTime(new Date());
        String releaseDate = parseItem.getReleaseDate();
        if (StringUtils.isNotEmpty(releaseDate)) {
            clNews.setOriginalTime(DateUtils.stringToDate(releaseDate, DateUtils.DATE_TIME_FORMAT_CHINESE));
        }
        return clNews;
    }

    /**
     * 获取图文类型。
     *
     * @param htmlLabels
     * @return
     */
    private int getDocType(List<HtmlLabel> htmlLabels) {
        int type = 0;
        int num = 0;
        if (null != htmlLabels && !htmlLabels.isEmpty()) {
            for (HtmlLabel htmlLabel : htmlLabels) {
                if (htmlLabel.getType().equals(CrawlerEnum.HtmlType.IMG_TAG)) {
                    num++;
                }
            }
        }
        if (num == 0) {
            type = 0;
        } else if (num == 1) {
            type = 1;
        } else {
            type = 2;
        }
        return type;
    }

    /**
     * 前置参数处理。
     * readCount ~ 阅读数 200。
     *
     * @param itemsAll
     */
    @Override
    public void preParameterHandle(Map<String, Object> itemsAll) {
        String readCount = HMStringUtils.toString(itemsAll.get("readCount"));
        if (StringUtils.isNotEmpty(readCount)) {
            readCount = readCount.split(" ")[1];
            if (StringUtils.isNotEmpty(readCount)) {
                itemsAll.put("readCount", readCount);
            }
        }
    }

    @Override
    public int getPriority() {
        return 1000;
    }

}
