package com.geek.crawler.process;

import com.alibaba.fastjson.JSON;
import com.geek.crawler.config.CrawlerConfig;
import com.geek.crawler.factory.CrawlerProxyFactory;
import com.geek.crawler.helper.CookieHelper;
import com.geek.crawler.helper.CrawlerHelper;
import com.geek.crawler.utils.HttpClientUtils;
import com.geek.crawler.utils.JsonValidator;
import com.geek.crawler.utils.RequestUtils;
import com.geek.crawler.utils.SeleniumClient;
import com.geek.model.crawler.core.cookie.CrawlerCookie;
import com.geek.model.crawler.core.cookie.CrawlerHtml;
import com.geek.model.crawler.core.parse.ParseItem;
import com.geek.model.crawler.core.proxy.CrawlerProxy;
import com.geek.model.crawler.core.proxy.CrawlerProxyProvider;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Request;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * IProcessFlow 的抽象模板类，对其他子类通用方法的一些抽取。
 * 已经模板抽象方法的一些定义。
 */
@Log4j2
public abstract class AbstractProcessFlow implements IProcessFlow {

    /**
     * UserAgent 参数设置。
     */
    public final String UserAgentParameterName = "User-Agent";
    /**
     * UserAgent 参数设置。
     */
    public final String AcceptParameterName = "Accept";
    /**
     * UA。
     * user agent 意思是用户代理。用户代理是一种对数据打包、创造分组头，以及编址、传递消息的部件。
     * 用户代理是指浏览器，它的信息包括硬件平台、系统软件、应用软件和用户个人偏好。用户代理，它还包括搜索引擎。
     * User Agent 中文名为用户代理，简称 UA，它是一个特殊字符串头，使得服务器能够识别客户使用的操作系统及版本、CPU 类型、浏览器及版本、浏览器渲染引擎、浏览器语言、浏览器插件等。
     */
    private final String[] UserAgent = {
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:65.0) Gecko/20100101 Firefox/65.0",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0.3 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) CriOS/31.0.1650.18 Mobile/11B554a Safari/8536.25",
            "Mozilla/5.0 (iPhone; CPU iPhone OS 8_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12F70 Safari/600.1.4",
            "Mozilla/5.0 (Linux; Android 4.2.1; M040 Build/JOP40D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.59 Mobile Safari/537.36",
            "Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; M351 Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30"
    };
    /**
     * 设置 Accept。
     * Accept 请求头用来告知（服务器）客户端可以处理的内容类型，这种内容类型用 MIME 类型来表示。借助内容协商机制，服务器可以从诸多备选项中选择一项进行应用，并使用 Content-Type 应答头通知客户端它的选择。浏览器会基于请求的上下文来为这个请求头设置合适的值，比如获取一个 CSS 层叠样式表时值与获取图片、视频或脚本文件时的值是不同的。
     */
    private final String[] Accept = {
            "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"
    };

    @Autowired
    private CrawlerConfig crawlerConfig;
    @Autowired
    private CrawlerHelper crawlerHelper;
    @Autowired
    private CookieHelper cookieHelper;
    @Autowired
    private SeleniumClient seleniumClient;
    @Autowired
    private CrawlerProxyProvider crawlerProxyProvider;

    /**
     * 获取 header 头。
     *
     * @return
     */
    public Map<String, String> getHeaderMap() {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(UserAgentParameterName, getUserAgent());
        headerMap.put(AcceptParameterName, getAccept());
        return headerMap;
    }

    /**
     * request 封装。
     *
     * @param url
     * @return
     */
    public Request getRequest(String url) {
        Map<String, String> headerMap = getHeaderMap();
        Request request = RequestUtils.requestPackage(url, headerMap);
        return request;
    }

    /**
     * request 封装。
     *
     * @param parseItem
     * @return
     */
    public Request getRequest(ParseItem parseItem) {
        Request request = null;
        String initialUrl = parseItem.getInitialUrl();
        if (StringUtils.isNotEmpty(initialUrl)) {
            request = getRequest(initialUrl);
            crawlerHelper.setParseItem(request, parseItem);
        }
        return request;
    }

    /**
     * 添加 request。
     *
     * @param parseItemList
     */
    public void addSpiderRequest(List<ParseItem> parseItemList) {
        if (null != parseItemList && !parseItemList.isEmpty()) {
            for (ParseItem parseItem : parseItemList) {
                Request request = getRequest(parseItem);
                crawlerConfig.getSpider().addRequest(request);
            }
        }
    }

    /**
     * 获取随机 UA。
     */
    public String getUserAgent() {
        return UserAgent[(int) (Math.random() * (UserAgent.length))];
    }

    /**
     * 获取随机 Accept。
     */
    public String getAccept() {
        return Accept[(int) (Math.random() * (Accept.length))];
    }

    /**
     * 获取原始的 Html 页面数据。
     *
     * @param url
     * @param parameterMap
     * @return
     */
    public String getOriginalRequestHtmlData(String url, Map<String, String> parameterMap) {
        // 获取代理。
        CrawlerProxy proxy = crawlerProxyProvider.getRandomProxy();
        // 获取 Cookie 列表。
        List<CrawlerCookie> cookieList = cookieHelper.getCookieEntity(url, proxy);
        // 通过 HttpClient 方式来获取数据。
        String htmlData = getHttpClientRequestData(url, parameterMap, cookieList, proxy);
        boolean isValidate = crawlerHelper.getDataValidateCallBack().validate(htmlData);
        if (!isValidate) {
            CrawlerHtml crawlerHtml = getSeleniumRequestData(url, parameterMap, proxy);
            htmlData = crawlerHtml.getHtml();
        }
        return htmlData;
    }

    /**
     * 通过 Http Client 来获取数据。
     *
     * @param url          请求的 URL。
     * @param parameterMap 参数。
     * @param cookieList   cookie 列表。
     * @param crawlerProxy 代理。
     * @return
     */
    public String getHttpClientRequestData(String url, Map<String, String> parameterMap, List<CrawlerCookie> cookieList, CrawlerProxy crawlerProxy) {
        CookieStore cookieStore = getCookieStore(cookieList);
        String jsonDate = null;
        HttpHost proxy = null;
        if (null != crawlerProxy) {
            proxy = CrawlerProxyFactory.getHttpHostProxy(crawlerProxy);
        }
        try {
            long currentTime = System.currentTimeMillis();
            log.info("HttpClient 请求数据。url：{}，parameter：{}，cookies：{}，proxy：{}", url, parameterMap, JSON.toJSONString(cookieList), proxy);
            jsonDate = HttpClientUtils.get(url, parameterMap, getHeaderMap(), cookieStore, proxy, "UTF-8");
            log.info("HttpClient 请求数据完成。url：{}，parameter：{}，cookies：{}，proxy：{}，duration：{}，result：{}", url, parameterMap, JSON.toJSONString(cookieList), proxy, System.currentTimeMillis() - currentTime, jsonDate);
        } catch (IOException e) {
            log.error("HttpClient 请求数据异常。url：{}，parameter：{}，cookies：{}，proxy：{}，errorMsg：{}", url, parameterMap, JSON.toJSONString(cookieList), proxy, e.getMessage());
        } catch (URISyntaxException e) {
            log.error("HttpClient 请求数据异常。url：{}，parameter：{}，cookies：{}，proxy：{}，errorMsg：{}", url, parameterMap, JSON.toJSONString(cookieList), proxy, e.getMessage());
        }
        return jsonDate;
    }

    /**
     * 获取 SeleniumRequestData。
     *
     * @param url
     * @param parameterMap
     * @return
     */
    public CrawlerHtml getSeleniumRequestData(String url, Map<String, String> parameterMap, CrawlerProxy proxy) {
        String buildUrl = HttpClientUtils.buildGetUrl(url, parameterMap, HttpClientUtils.utf8);
        String cookieName = cookieHelper.getCookieName();
        CrawlerHtml crawlerHtml = seleniumClient.getCrawlerHtml(buildUrl, proxy, cookieName);
        if (null != crawlerHtml) {
            cookieHelper.updateCookie(crawlerHtml.getCrawlerCookieList(), proxy);
        }
        return crawlerHtml;
    }

    /**
     * cookie 转 CookieStore。
     *
     * @param cookieList
     * @return
     */
    private CookieStore getCookieStore(List<CrawlerCookie> cookieList) {
        BasicCookieStore cookieStore = null;
        if (null != cookieList && !cookieList.isEmpty()) {
            for (CrawlerCookie cookie : cookieList) {
                if (null != cookie) {
                    BasicClientCookie basicClientCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
                    basicClientCookie.setDomain(cookie.getDomain());
                    basicClientCookie.setPath(cookie.getPath());
                    cookieStore = new BasicCookieStore();
                    cookieStore.addCookie(basicClientCookie);
                }
            }
        }
        return cookieStore;
    }

    /**
     * 获取原始的请求的 JSON 数据。
     *
     * @param url
     * @param parameterMap
     * @return
     */
    public String getOriginalRequestJsonData(String url, Map<String, String> parameterMap) {
        // 获取代理。
        CrawlerProxy proxy = crawlerProxyProvider.getRandomProxy();
        // 获取 Cookie 列表。
        List<CrawlerCookie> cookieList = cookieHelper.getCookieEntity(url, proxy);
        // 通过 HttpClient 方式来获取数据。
        String jsonData = getHttpClientRequestData(url, parameterMap, cookieList, proxy);
        // 如果不是 JSON，说明数据抓取失败。则通过 SeleniumUtils 的方式来获取数据。
        if (!isJson(jsonData)) {
            CrawlerHtml crawlerHtml = getSeleniumRequestData(url, parameterMap, proxy);
            jsonData = seleniumClient.getJsonData(crawlerHtml);
        }
        return jsonData;
    }

    /**
     * 验证字符串是否是 json 格式。
     *
     * @param jsonData
     * @return
     */
    public boolean isJson(String jsonData) {
        boolean isJson = false;
        try {
            isJson = JsonValidator.getJsonValidator().validate(jsonData);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return isJson;
    }

}
