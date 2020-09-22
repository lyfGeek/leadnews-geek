package com.geek.model.crawler.core.delayed;

import com.geek.model.crawler.core.callback.IConcurrentCallBack;
import com.geek.model.crawler.core.callback.IDelayedCallBack;

/**
 * 延时调用工具类。
 */
public class DelayedUtils {

    public static void delayed(long delayedTime) {
        try {
            Thread.sleep(delayedTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 延时方法。
     *
     * @param callBack
     * @return
     */
    public static Object delayed(IDelayedCallBack callBack) {
        boolean flag = false;
        long sleepTime = callBack.sleepTime();
        long timeOut = callBack.timeOut();
        long currentTime = System.currentTimeMillis();
        Object obj = null;
        while (true) {
            try {
                Thread.sleep(sleepTime);// 延时秒数。
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long duration = System.currentTimeMillis() - currentTime;
            boolean isExist = callBack.isExist();
            obj = callBack.callBack(duration);
            if (isExist) {// cookie 存在就 break。
                flag = true;
            } else if (duration > timeOut) {
                flag = true;
            }
            if (flag) {
                break;
            }
        }
        return obj;
    }

    /**
     * 并发过滤。
     *
     * @param time
     * @return
     */
    public synchronized static IConcurrentCallBack getConcurrentFilter(final long time) {
        final ConcurrentEntity concurrentEntity = new ConcurrentEntity();
        concurrentEntity.setTimeInterval(time);
        return new IConcurrentCallBack() {
            public boolean filter() {
                boolean flag = false;
                // 数据初始化。
                long duration = System.currentTimeMillis() - concurrentEntity.getCurrentTime();
                if (duration > time) {
                    concurrentEntity.setAvailable(true);
                    concurrentEntity.setCallCount(0);
                    concurrentEntity.setCurrentTime(System.currentTimeMillis());
                }
                long callCount = concurrentEntity.getCallCount();
                concurrentEntity.setCallCount(++callCount);
                if (callCount <= 1 && concurrentEntity.isAvailable()) {// 多个并发，只取一个。
                    flag = true;
                    concurrentEntity.setAvailable(false);
                }
                return flag;
            }
        };
    }


    static class ConcurrentEntity {
        /**
         * 当前时间。
         */
        private long currentTime = System.currentTimeMillis();
        /**
         * 时间区间。
         */
        private long timeInterval = 10000;
        /**
         * 是否可用。
         */
        private boolean available = true;
        /**
         * 调用次数。
         */
        private long callCount = 0;

        public long getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(long currentTime) {
            this.currentTime = currentTime;
        }

        public long getTimeInterval() {
            return timeInterval;
        }

        public void setTimeInterval(long timeInterval) {
            this.timeInterval = timeInterval;
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }

        public long getCallCount() {
            return callCount;
        }

        public void setCallCount(long callCount) {
            this.callCount = callCount;
        }
    }

}
