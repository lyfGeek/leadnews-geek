package com.geek.common.common.storage;

import lombok.Getter;
import lombok.Setter;

/**
 * 存储 Entry。
 * k-v 结构保存一个对象的字段的字段名和值。
 */
@Setter
@Getter
public class StorageEntry {
    /**
     * 字段的 Key。
     */
    private String key;
    /**
     * 字段的 Value。
     */
    private String value;

    /**
     * 空的构造方法。
     */
    public StorageEntry() {
    }

    /**
     * 构造方法。
     *
     * @param key
     * @param value
     */
    public StorageEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
