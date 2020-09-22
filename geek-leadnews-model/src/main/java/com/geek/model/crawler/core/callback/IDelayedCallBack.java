package com.geek.model.crawler.core.callback;

/**
 * 延时回调接口。
 * 为了判断下载页面是否成功。
 * 因为 csdn 的 cookie 登录验证是通过 js 实现的，
 * 需要通过 Selenium 下载页面后等待一会检测 cookie 是否注入成功。
 */
public interface IDelayedCallBack {

    /**
     * 延时调用方法。
     *
     * @param time
     * @return
     */
    Object callBack(long time);

    /**
     * 判断是否存在。
     *
     * @return
     */
    boolean isExist();

    /**
     * 每次睡眠时间。
     *
     * @return
     */
    long sleepTime();

    /**
     * 超时时间。
     *
     * @return
     */
    long timeOut();

}
