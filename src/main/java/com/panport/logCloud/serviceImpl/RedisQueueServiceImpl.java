package com.panport.logCloud.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.panport.logCloud.constant.Constant;
import com.panport.logCloud.entity.alarm.AlarmBean;
import com.panport.logCloud.service.AlarmService;
import com.panport.logCloud.service.RedisQueueService;
import com.panport.logCloud.utils.DistributedRedisClusterStore;
import com.panport.logCloud.utils.ObjectAndByteTran;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by minisheep on 2019/8/22.
 */
@Service
public class RedisQueueServiceImpl implements RedisQueueService {
    private JedisCluster jedisCluster = DistributedRedisClusterStore.getJedisCluster();

    @Autowired
    private AlarmService alarmService;

    @Override
    public void dealWithRedisQueueData() {
        while (true) {
            if (jedisCluster.llen(ObjectAndByteTran.toByteArray(Constant.ALARM_KRY)) > 0) {
                byte[] popValue = jedisCluster.rpop(ObjectAndByteTran.toByteArray(Constant.ALARM_KRY));
                Map<String, Object> obj = (Map<String, Object>) ObjectAndByteTran.toObject(popValue);
                String json = alarmService.sqlSearch((String) obj.get(Constant.CONTENT));
                JSONObject jsonObject = JSONObject.parseObject(json);
                AlarmBean alarmBeans = jsonObject.toJavaObject(AlarmBean.class);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String resultDate = sdf.format(new Date());
                String ruleEnName = (String) obj.get(Constant.RULEENNAME);
                int total = alarmBeans.getHits().getTotal();
                System.out.println("规则代号：" + ruleEnName + "，于日期 " + resultDate +
                        " 找到共计"+ total + "条满足您所设定的规则预警，请您登陆预警平台查看详细信息");


                // 规则代号：xxxx，于new Date() 共计xx条满足您所设定的规则预警，请您登陆预警平台查看详细信息。
                // 在此可以判断该规则代号在一段时间内是否重复提醒，如果重复提醒就按用户设置的提醒频率来，
                // 如果该规则代号在一段时间内没有出现过，则马上提醒
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
