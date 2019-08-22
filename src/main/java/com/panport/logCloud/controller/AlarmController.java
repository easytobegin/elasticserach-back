package com.panport.logCloud.controller;

import com.panport.logCloud.service.AlarmService;
import com.pantech.cloud.common.msg.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by minisheep on 2019/7/25.
 */
@RestController
@RequestMapping(value = "/alarm")
public class AlarmController {
    @Autowired
    private AlarmService alarmService;

    // elasticsearch-sql查询
    @RequestMapping(value = "/sql", method = RequestMethod.POST)
    public Message elasticsearchSqlSearch(@RequestBody Map<String, Object> map){
        try {
            String sql = (String) map.get("sql");

            String result = alarmService.sqlSearch(sql);
            if (result != null && !result.equals("")) {
                return Message.ok(result);
            } else {
                return Message.fail("该sql无效！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Message.fail("sql查询失败！");
        }
    }

}
