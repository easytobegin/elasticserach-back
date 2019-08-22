package com.panport.logCloud.serviceImpl;

import com.panport.logCloud.service.RedisQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by minisheep on 2019/8/22.
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Autowired
    private RedisQueueService redisQueueService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        redisQueueService.dealWithRedisQueueData();
    }
}
