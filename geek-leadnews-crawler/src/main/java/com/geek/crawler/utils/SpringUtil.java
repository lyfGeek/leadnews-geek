package com.geek.crawler.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

/**
 * spring 工具类。
 * 通过该工具类可以进行获取 bean 的实例。
 */
public class SpringUtil {

    private static ApplicationContext applicationContext;

    // 获取 applicationContext。
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 设置 applicationContext。
     *
     * @param applicationContext
     * @throws BeansException
     */
    public static void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
    }

    // 通过 name 获取 Bean。
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    // 通过 class 获取 Bean。
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    // 通过 name 以及 Clazz 返回指定的 Bean。
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
