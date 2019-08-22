package com.panport.logCloud.timer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.panport.logCloud.constant.Constant;
import com.panport.logCloud.entity.alarm.AlarmBean;
import com.panport.logCloud.service.AlarmService;
import com.panport.logCloud.utils.DistributedRedisClusterStore;
import com.panport.logCloud.utils.ObjectAndByteTran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by minisheep on 2019/8/22.
 */
@Component
public class AlarmTimer {

    @Autowired
    private AlarmService alarmService;

    private JedisCluster jedisCluster = DistributedRedisClusterStore.getJedisCluster();

    // 每20s对每个预警都执行一遍
    @Scheduled(fixedRate = 20000)
    public void alarmScheduled() {
        String json = alarmService.sqlSearch("select content,enable,ruleEnName,timestamp from rules-store");
        JSONObject jsonObject = JSONObject.parseObject(json);
        AlarmBean alarmBeans = jsonObject.toJavaObject(AlarmBean.class);
        alarmBeans.getHits().getHits().forEach(alarmBean -> {
            // 启用才添加
            if (alarmBean.get_source().getEnable() == true) {
                Map<String, Object> map = new HashMap<>();
                // 获取规则英文名
                map.put(Constant.RULEENNAME, alarmBean.get_source().getRuleEnName());
                map.put(Constant.CONTENT, alarmBean.get_source().getContent());
                jedisCluster.lpush(ObjectAndByteTran.toByteArray(Constant.ALARM_KRY), ObjectAndByteTran.toByteArray(map));  // 用rpop取
            }
        });
//        System.out.println(monitors.toString());
    }
}
