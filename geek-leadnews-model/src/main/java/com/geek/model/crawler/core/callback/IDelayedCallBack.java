package com.geek.model.crawler.core.callback;

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
     * 获取每次睡眠时间。
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
