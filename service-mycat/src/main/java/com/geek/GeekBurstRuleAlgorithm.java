package com.geek;

import io.mycat.config.model.rule.RuleAlgorithm;
import io.mycat.route.function.AbstractPartitionAlgorithm;
import lombok.Data;

/**
 * 使用 Maven 打 jar 包。
 * 放入 Mycat 的 lib 目录下。
 */
@Data
public class GeekBurstRuleAlgorithm extends AbstractPartitionAlgorithm implements RuleAlgorithm {

    /**
     * 单组容量。
     */
    private Long volume;
    /**
     * 单组节点量。
     */
    private Integer step;
    /**
     * 单组数据 mod。
     */
    private Integer mod;

    /**
     * 分片 ID = （dataId / volume）* step + 分表ID / mod
     *
     * @param columnValue 3-2 1-2
     * @return
     */
    @Override
    public Integer calculate(String columnValue) {
        if (columnValue != null) {
            String[] temp = columnValue.split("-");
            if (temp.length == 2) {
                try {
                    Long dataId = Long.valueOf(temp[0]);
                    Long burstId = Long.valueOf(temp[1]);
                    int group = (int) (dataId / volume) * step;
                    int pos = group + (int) (burstId / mod);
                    System.out.println("geek RULE INFO [" + columnValue + "] - [{" + pos + "}]");
                    return pos;
                } catch (Exception e) {
                    System.out.println("geek RULE INFO [" + columnValue + "] - [{" + e.getMessage() + "}]");
                }
            }
        }
        return 0;
    }

    @Override
    public Integer[] calculateRange(String beginValue, String endValue) {
//        return super.calculateRange(beginValue, endValue);
        if (beginValue != null && endValue != null) {
            Integer begin = calculate(beginValue);
            Integer end = calculate(endValue);
            if (begin == null || end == null) {
                return new Integer[0];
            }
            if (end >= begin) {
                int length = end - begin + 1;
                Integer[] re = new Integer[length];
                for (int i = 0; i < length; i++) {
                    re[i] = begin + i;
                }
                return re;
            }
        }
        return new Integer[0];
    }

}
