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
        StringBuffer stringBuffer = new StringBuffer();
        if (fields != null && fields.length > 0) {
            stringBuffer.append(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                stringBuffer.append(SPLIT_CHAR).append(fields[i]);
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 默认第一组。
     *
     * @param fields
     * @return
     */
    public static String groundOne(Object... fields) {
        StringBuffer stringBuffer = new StringBuffer();
        if (fields != null && fields.length > 0) {
            stringBuffer.append("0");
            for (int i = 0; i < fields.length; i++) {
                stringBuffer.append(SPLIT_CHAR).append(fields[i]);
            }
        }
        return stringBuffer.toString();
    }

}
