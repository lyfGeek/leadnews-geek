package com.geek.crawler.config;

import com.geek.crawler.helper.CookieHelper;
import com.geek.crawler.helper.CrawlerHelper;
import com.geek.crawler.process.entity.CrawlerConfigProperty;
import com.geek.crawler.process.scheduler.DbAndRedisScheduler;
import com.geek.crawler.service.ICrawlerIpPoolService;
import com.geek.crawler.utils.SeleniumClient;
import com.geek.model.crawler.core.callback.IDataValidateCallBack;
import com.geek.model.crawler.core.callback.IProxyProviderCallBack;
import com.geek.model.crawler.core.parse.ParseRule;
import com.geek.model.crawler.core.proxy.CrawlerProxy;
import com.geek.model.crawler.core.proxy.CrawlerProxyProvider;
import com.geek.model.crawler.enums.CrawlerEnum;
import com.geek.model.crawler.pojos.ClIpPool;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Spider;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Configuration
@PropertySource("classpath:crawler.properties")
@ConfigurationProperties(prefix = "crawler.init.url")
public class CrawlerConfig {

    private String prefix;//https://www.csdn.net/nav/
    private String suffix;//java,web,arch,db,mobile,ops,sec

    @Value("${crux.cookie.name}")
    private String CRUX_COOKIE_NAME;
    @Value("${proxy.isUsedProxyIp}")
    private Boolean isUsedProxyIp;
    @Autowired
    private ICrawlerIpPoolService crawlerIpPoolService;
    private String initCrawlerXpath = "//ul[@class='feedlist_mod']/li[@class='clearfix']/div[@class='list_con']/dl[@class='list_userbar']/dd[@class='name']/a";
    private String helpCrawlerXpath = "//div[@class='article-list']/div[@class='article-item-box']/h4/a";
    @Value("${crawler.help.nextPagingSize}")
    private Integer crawlerHelpNextPagingSize;
    @Value("${redis.host}")
    private String redisHost;
    @Value("${redis.port}")
    private int reidsPort;
    @Value("${redis.timeout}")
    private int reidstimeout;
    @Value("${redis.password}")
    private String reidsPassword;
    private Spider spider;

    public List<String> getInitCrawlerUrlList() {
        List<String> initCrawlerUrlList = new ArrayList<>();
        if (StringUtils.isNotEmpty(suffix)) {
            String[] urlArray = suffix.split(",");
            if (urlArray != null && urlArray.length > 0) {
                for (int i = 0; i < urlArray.length; i++) {
                    String initUrl = urlArray[i];
                    if (StringUtils.isNotEmpty(initUrl)) {
                        if (!initUrl.toLowerCase().startsWith("http")) {
                            initCrawlerUrlList.add(prefix + initUrl);
                        }
                    }
                }
            }
        }
        return initCrawlerUrlList;
    }

    @Bean
    public SeleniumClient getSeleniumClient() {
        return new SeleniumClient();
    }

    /**
     * 设置 Cookie 辅助类。
     *
     * @return
     */
    @Bean
    public CookieHelper getCookieHelper() {
        return new CookieHelper(CRUX_COOKIE_NAME);
    }

    /**
     * 数据校验匿名内部类。
     *
     * @param cookieHelper
     * @return
     */
    private IDataValidateCallBack getDataValidateCallBack(CookieHelper cookieHelper) {
        return new IDataValidateCallBack() {
            @Override
            public boolean validate(String content) {
                boolean flag = true;
                if (StringUtils.isEmpty(content)) {
                    flag = false;
                } else {
                    boolean isContains_acw_sc_v2 = content.contains("acw_sc__v2");
                    boolean isContains_location_reload = content.contains("document.location.reload()");
                    if (isContains_acw_sc_v2 && isContains_location_reload) {
                        flag = false;
                    }
                }
                return flag;
            }
        };
    }

    /**
     * CrawlerHelper 辅助类。
     *
     * @return
     */
    @Bean
    public CrawlerHelper getCrawlerHelper() {
        CookieHelper cookieHelper = getCookieHelper();
        CrawlerHelper crawlerHelper = new CrawlerHelper();
        IDataValidateCallBack dataValidateCallBack = getDataValidateCallBack(cookieHelper);
        crawlerHelper.setDataValidateCallBack(dataValidateCallBack);
        return crawlerHelper;
    }

    /**
     * CrawlerProxyProvider bean。
     *
     * @return
     */
    @Bean
    public CrawlerProxyProvider getCrawlerProxyProvider() {
        CrawlerProxyProvider crawlerProxyProvider = new CrawlerProxyProvider();
        crawlerProxyProvider.setUsedProxyIp(isUsedProxyIp);
        //设置动态代理
        crawlerProxyProvider.setProxyProviderCallBack(new IProxyProviderCallBack() {
            @Override
            public List<CrawlerProxy> getProxyList() {
                return getCrawlerProxyList();
            }

            @Override
            public void unavailable(CrawlerProxy crawlerProxy) {
                unavailableProxy(crawlerProxy);
            }
        });
        return crawlerProxyProvider;
    }

    /**
     * 获取初始化的 ip 列表。
     *
     * @return
     */
    public List<CrawlerProxy> getCrawlerProxyList() {
        List<CrawlerProxy> crawlerProxyList = new ArrayList<>();
        ClIpPool clIpPool = new ClIpPool();
        clIpPool.setDuration(5);
        List<ClIpPool> clIpPools = crawlerIpPoolService.queryAvailabelList(clIpPool);
        if (null != clIpPools && !clIpPools.isEmpty()) {
            for (ClIpPool ipPool : clIpPools) {
                crawlerProxyList.add(new CrawlerProxy(ipPool.getIp(), ipPool.getPort()));
            }
        }

        return crawlerProxyList;
    }

    /**
     * 代理 ip 不可用处理方法。
     *
     * @param crawlerProxy
     */
    public void unavailableProxy(CrawlerProxy crawlerProxy) {
        if (crawlerProxy != null) {
            crawlerIpPoolService.unAvailableProxy(crawlerProxy, "自动禁用");
        }
    }

    @Bean
    public CrawlerConfigProperty getCrawlerConfigProperty() {
        CrawlerConfigProperty property = new CrawlerConfigProperty();
        // 初始化 url 列表。
        property.setInitCrawlerUrlList(getInitCrawlerUrlList());
        // 初始化 url 解析规则定义。
        property.setInitCrawlerXpath(initCrawlerXpath);
        // 用户空间下的解析规则 url。
        property.setHelpCrawlerXpath(helpCrawlerXpath);
        // 抓取用户空间下的页大小。
        property.setCrawlerHelpNextPagingSize(crawlerHelpNextPagingSize);
        // 目标页的解析规则。
        property.setTargetParseRuleList(getTargetParseRuleList());

        return property;
    }

    /**
     * 目标页的解析规则。
     *
     * @return
     */
    private List<ParseRule> getTargetParseRuleList() {
        List<ParseRule> parseRules = new ArrayList<ParseRule>() {{
            // 标题。
            add(new ParseRule("title", CrawlerEnum.ParseRuleType.XPATH, "//h1[@class='title-article']/text()"));
            // 作者。
            add(new ParseRule("author", CrawlerEnum.ParseRuleType.XPATH, "//a[@class='follow-nickName']/text()"));
            // 发布日期。
            add(new ParseRule("releaseDate", CrawlerEnum.ParseRuleType.XPATH, "//span[@class='time']/text()"));
            // 标签。
            add(new ParseRule("labels", CrawlerEnum.ParseRuleType.XPATH, "//span[@class='tags-box']/a/text()"));
            // 个人空间。
            add(new ParseRule("personalSpace", CrawlerEnum.ParseRuleType.XPATH, "//a[@class='follow-nickName']/@href"));
            // 阅读量。
            add(new ParseRule("readCount", CrawlerEnum.ParseRuleType.XPATH, "//span[@class='read-count']/text()"));
            // 点赞量。
            add(new ParseRule("likes", CrawlerEnum.ParseRuleType.XPATH, "//div[@class='tool-box']/ul[@class='meau-list']/li[@class='btn-like-box']/button/p/text()"));
            // 回复次数。
            add(new ParseRule("commentCount", CrawlerEnum.ParseRuleType.XPATH, "//div[@class='tool-box']/ul[@class='meau-list']/li[@class='to-commentBox']/a/p/text()"));
            // 文章内容。
            add(new ParseRule("content", CrawlerEnum.ParseRuleType.XPATH, "//div[@id='content_views']/html()"));
        }};
        return parseRules;
    }

    @Bean
    public DbAndRedisScheduler getDbAndRedisScheduler() {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        JedisPool jedisPool = new JedisPool(genericObjectPoolConfig, redisHost, reidsPort, reidstimeout, null, 0);
        return new DbAndRedisScheduler(jedisPool);
    }

    public Spider getSpider() {
        return spider;
    }

    public void setSpider(Spider spider) {
        this.spider = spider;
    }

}
