//package com.geek.article.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.geek.article.service.IApHotArticleService;
//import com.geek.common.article.constans.ArticleConstant;
//import com.geek.common.kafka.KafkaSender;
//import com.geek.model.admin.pojos.AdChannel;
//import com.geek.model.article.pojos.ApArticle;
//import com.geek.model.article.pojos.ApHotArticles;
//import com.geek.model.behavior.pojos.ApBehaviorEntry;
//import com.geek.model.mappers.admin.IAdChannelMapper;
//import com.geek.model.mappers.app.IApArticleMapper;
//import com.geek.model.mappers.app.IApBehaviorEntryMapper;
//import com.geek.model.mappers.app.IApHotArticlesMapper;
//import org.joda.time.DateTime;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ApHotArticleServiceImpl implements IApHotArticleService {
//
//    @Autowired
//    private IApArticleMapper apArticleMapper;
//
//    @Autowired
//    private IApBehaviorEntryMapper apBehaviorEntryMapper;
//
//    @Autowired
//    private IApHotArticlesMapper apHotArticlesMapper;
//
//    @Autowired
//    private KafkaSender kafkaSender;
//
//    @Autowired
//    private IAdChannelMapper adChannelMapper;
//
//    @Autowired
//    private StringRedisTemplate redisTemplate;
//
//    @Override
//    public void computeHotArticle() {
//        // 查询前一天的新发布的文章列表。
//        String lastDay = DateTime.now().minusDays(1).toString("yyyy-MM-dd 00:00:00");
//        List<ApArticle> articleList = apArticleMapper.loadLastArticleForHot(lastDay);
//        // 计算文章热度（阅读量，评论，点赞，收藏）。
//        List<ApHotArticles> hotArticlesList = computeHotArticle(articleList);
//        // 缓存频道首页到 redis。
//        cacheTagRoRedis(articleList);
//        // 给每一个用户保存一份热点文章。
//        List<ApBehaviorEntry> entryList = apBehaviorEntryMapper.selectAllEntry();
//        for (ApHotArticles hot : hotArticlesList) {
//            // 保存热点文章。
//            apHotArticlesMapper.insert(hot);
//            // 给每一个用户保存热点数据。
//            saveHotArticleForEntryList(hot, entryList);
//            // 缓存文章中的图片。
//            kafkaSender.sendHotArticleMessage(hot);
//        }
//    }
//
//    /**
//     * 给每一个用户保存一份热点数据。
//     *
//     * @param hot
//     * @param entryList
//     */
//    private void saveHotArticleForEntryList(ApHotArticles hot, List<ApBehaviorEntry> entryList) {
//        for (ApBehaviorEntry entry : entryList) {
//            hot.setEntryId(entry.getId());
//            apHotArticlesMapper.insert(hot);
//        }
//    }
//
//    /**
//     * 缓存频道首页数据到 redis。
//     *
//     * @param articleList
//     */
//    private void cacheTagRoRedis(List<ApArticle> articleList) {
//        List<AdChannel> channels = adChannelMapper.selectAll();
//        List<ApArticle> temp = null;
//        for (AdChannel channel : channels) {
//            temp = articleList.stream().filter(p -> p.getChannelId().equals(channel.getId())).collect(Collectors.toList());
//
//            if (temp.size() > 30) {
//                temp = temp.subList(0, 30);
//            }
//            if (temp.size() == 0) {
//                redisTemplate.opsForValue().set(ArticleConstant.HOT_ARTICLE_FIRST_PAGE + channel.getId(), "");
//                continue;
//            }
//
//            redisTemplate.opsForValue().set(ArticleConstant.HOT_ARTICLE_FIRST_PAGE + channel.getId(), JSON.toJSONString(temp));
//        }
//    }
//
//    /**
//     * 计算文章的分值。
//     *
//     * @param apArticles
//     * @return
//     */
//    public List<ApHotArticles> computeHotArticle(List<ApArticle> apArticles) {
//        List<ApHotArticles> hotArticlesList = new ArrayList<>();
//        ApHotArticles hot = null;
//        for (ApArticle apArticle : apArticles) {
//            hot = initHotBaseApArticle(apArticle);
//            Integer score = computeScore(apArticle);
//            hot.setScore(score);
//            hotArticlesList.add(hot);
//        }
//        // 通过排序取出前 1000 条数据。
//        hotArticlesList.sort(new Comparator<ApHotArticles>() {
//            @Override
//            public int compare(ApHotArticles o1, ApHotArticles o2) {
//                return o1.getScore() < o2.getScore() ? 1 : -1;
//            }
//        });
//        if (hotArticlesList.size() > 1000) {
//            return hotArticlesList.subList(0, 1000);
//        }
//
//        return hotArticlesList;
//    }
//
//    /**
//     * 计算分值。
//     *
//     * @param apArticle
//     * @return
//     */
//    private Integer computeScore(ApArticle apArticle) {
//        Integer score = 0;
//        if (apArticle.getLikes() != null) {
//            score += apArticle.getLikes();
//        }
//        if (apArticle.getCollection() != null) {
//            score += apArticle.getCollection();
//        }
//        if (apArticle.getComment() != null) {
//            score += apArticle.getComment();
//        }
//        if (apArticle.getViews() != null) {
//            score += apArticle.getViews();
//        }
//
//        return score;
//    }
//
//    /**
//     * 初始化热度文章信息。
//     *
//     * @param apArticle
//     * @return
//     */
//    private ApHotArticles initHotBaseApArticle(ApArticle apArticle) {
//        ApHotArticles hot = new ApHotArticles();
//        hot.setEntryId(0);
//        hot.setTagId(apArticle.getChannelId());
//        hot.setTagName(apArticle.getChannelName());
//        hot.setScore(0);
//        hot.setArticleId(apArticle.getId());
//        hot.setProvinceId(apArticle.getProvinceId());
//        hot.setCityId(apArticle.getCityId());
//        hot.setCountyId(apArticle.getCountyId());
//        hot.setIsRead(0);
//        hot.setReleaseDate(apArticle.getPublishTime());
//        hot.setCreatedTime(new Date());
//        return hot;
//    }
//
//}
