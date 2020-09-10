package com.geek.common.HBase.entity;

/**
 * HBase 的回调类。
 * 用于我们操作的时候就行回调。
 */
public interface IHBaseInvoke {

    /**
     * 回调方法。
     */
    void invoke();

}
