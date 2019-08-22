package com.panport.logCloud.serviceImpl;

import com.panport.logCloud.service.AlarmService;
import com.panport.logCloud.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by minisheep on 2019/8/19.
 */
@Service
public class AlarmServiceImpl implements AlarmService {
    @Value("http://" + "${elasticsearchConfig.host}" + ":" + "${elasticsearchConfig.port}" + "${elasticsearchConfig._sql}")
    private String sqlUrl;

    @Override
    public String sqlSearch(String sql) {
        final String result = "{\"sql\":\"" + sql + "\"}";
        return HttpUtils.sendPostRequest(sqlUrl, result);
    }
}
