package com.panport.logCloud.utils;

import com.panport.logCloud.constant.Constant;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by minisheep on 2019/8/22.
 */
public class DistributedRedisClusterStore {
    private static JedisCluster cluster = null;
    private static JedisPoolConfig poolConfig = null;

    static {
        poolConfig = new JedisPoolConfig();
        // 设置最大连接数
        poolConfig.setMaxTotal(Constant.SINGLE_REDIS_MAXTOTAL);
        // 设置最大空闲数
        poolConfig.setMaxIdle(Constant.SINGLE_REDIS_MAXIDLE);
        // 设置最大等待时间
        poolConfig.setMaxWaitMillis(Constant.SINGLE_REDIS_MAXWAIT);
        // 在borrow一个jedis实例时，是否需要验证，若为true，则所有jedis实例均是可用的
        poolConfig.setTestOnBorrow(Constant.SINGLE_REDIS_BORROW);
        // 设置最大允许等待时间
        poolConfig.setMaxWaitMillis(Constant.SINGLE_REDIS_TIMEOUT);
    }

    // 高并发情况下会导致内存申请紧张，最后导致内存超出
    public synchronized static JedisCluster getJedisCluster() {

        String[] nodes = Constant.CLUSTER_IP.split(",");
        Set<HostAndPort> node = new LinkedHashSet<>();

        for (int i = 0; i < nodes.length; i++) {
            node.add(new HostAndPort(nodes[i].split(":")[0], Integer.valueOf(nodes[i].split(":")[1])));
        }
        if (cluster == null) {
            cluster = new JedisCluster(node, poolConfig);
        }

        return cluster;
    }
}
