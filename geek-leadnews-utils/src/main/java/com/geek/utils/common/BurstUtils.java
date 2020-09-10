package com.geek.utils.common;

/**
 * 分片桶字段算法。
 */
public class BurstUtils {

    public final static String SPLIT_CHAR = "-";

    /**
     * 用 - 符号连接.
     *
     * @param fields
     * @return
     */
    public static String encrypt(Object... fields) {
        StringBuffer sb = new StringBuffer();
        if (fields != null && fields.length > 0) {
            sb.append(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                sb.append(SPLIT_CHAR).append(fields[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 默认第一组。
     *
     * @param fields
     * @return
     */
    public static String groundOne(Object... fields) {
        StringBuffer sb = new StringBuffer();
        if (fields != null && fields.length > 0) {
            sb.append("0");
            for (int i = 0; i < fields.length; i++) {
                sb.append(SPLIT_CHAR).append(fields[i]);
            }
        }
        return sb.toString();
    }
}
