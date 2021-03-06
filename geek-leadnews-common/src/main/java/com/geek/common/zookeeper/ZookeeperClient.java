package com.geek.common.zookeeper;

import com.geek.common.zookeeper.sequence.ZkSequence;
import com.geek.common.zookeeper.sequence.ZkSequenceEnum;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Map;

@Setter
@Getter
public class ZookeeperClient {

    // 重试休眠时间。
    private final int SLEEP_TIME_MS = 1000;
    // 最大重试 1000 次。
    private final int MAX_RETRIES = 1000;
    // 会话超时时间。
    private final int SESSION_TIMEOUT = 30 * 1000;
    // 连接超时时间。
    private final int CONNECTION_TIMEOUT = 3 * 1000;

    private String host;
    private String sequencePath;

    private Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);
    private CuratorFramework client = null;
    // 序列化 map。线程安全 map。
    private Map<String, ZkSequence> zkSequenceMap = Maps.newConcurrentMap();

    /**
     * 初始化参数。
     *
     * @param host
     * @param sequencePath
     */
    public ZookeeperClient(String host, String sequencePath) {
        this.host = host;
        this.sequencePath = sequencePath;
    }

    /**
     * 初始化客户端。
     */
    @PostConstruct// 初始化类之前运行。
    public void init() {
        this.client = CuratorFrameworkFactory.builder()
                .connectString(this.getHost())
                .connectionTimeoutMs(CONNECTION_TIMEOUT)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(new ExponentialBackoffRetry(SLEEP_TIME_MS, MAX_RETRIES)).build();
        this.client.start();
        this.initZkSequence();
    }

    public void initZkSequence() {
        ZkSequenceEnum[] list = ZkSequenceEnum.values();
        for (int i = 0; i < list.length; i++) {
            String name = list[i].name();
            String path = this.sequencePath + name;
            ZkSequence zkSequence = new ZkSequence(this.client, path);
            zkSequenceMap.put(name, zkSequence);
        }
    }

    public Long sequence(ZkSequenceEnum name) {
        try {
            ZkSequence seq = zkSequenceMap.get(name.name());
            if (seq != null) {
                return seq.sequence();
            }
        } catch (Exception e) {
            logger.error("获取 [{}] Sequence 失败：{}", name, e);
            e.printStackTrace();
        }
        return null;
    }

}
