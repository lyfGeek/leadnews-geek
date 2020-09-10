package com.geek.images.service;

public interface ICacheImageService {

    /**
     * 缓存图片到 redis。
     *
     * @param imgUrl
     * @param isCache
     * @return
     */
    byte[] cache2Redis(String imgUrl, boolean isCache);

    /**
     * 延长图片缓存。
     *
     * @param imageKey
     */
    void resetCache2Redis(String imageKey);

}
