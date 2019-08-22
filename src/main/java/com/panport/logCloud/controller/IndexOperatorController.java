package com.panport.logCloud.controller;

import com.panport.logCloud.service.IndexOperatorService;
import com.pantech.cloud.common.msg.Message;
import com.pantech.cloud.common.msg.MessageBox;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by minisheep on 2019/8/14.
 */
@RestController
@RequestMapping(value = "/indexFunc")
public class IndexOperatorController {
    @Autowired
    private IndexOperatorService indexOperatorService;

    // 新增规则
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Message addRule(@RequestBody Map<String, Object> map){
        try {
            String indexName = (String) map.get("indexName");
            String type = (String) map.get("type");
            Map<String, Object> params = (Map<String, Object>) map.get("params");
            indexOperatorService.createIndex(indexName, type, params);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.fail("新建规则失败！");
        }
        return Message.ok("新建规则成功！");
    }

    // 删除规则
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Message deleteRule(@RequestBody Map<String, Object> map) {
        try {
            String indexName = (String) map.get("indexName");
            String type = (String) map.get("type");
            String id = (String) map.get("id");
            indexOperatorService.deleteIndex(indexName, type, id);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.fail("删除规则失败！");
        }

        return Message.ok("删除规则成功！");
    }

    // 修改规则
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Message modifyRule(@RequestBody Map<String, Object> map) {
        try {
            String indexName = (String) map.get("indexName");
            String type = (String) map.get("type");
            String id = (String) map.get("id");
            Map<String, Object> params = (Map<String, Object>) map.get("params");
            indexOperatorService.updateIndex(indexName, type, id, params);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.fail("修改规则失败！");
        }
        return Message.ok("修改规则成功！");
    }

    // 查询规则
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Message searchRule(@RequestBody Map<String, Object> map) {
        String indexName = (String) map.get("indexName");
        String type = (String) map.get("type");
        String id = (String) map.get("id");
        String result = indexOperatorService.searchIndex(indexName, type, id);

        if (result != null) {
            return new MessageBox<>(result);
        } else {
            return Message.fail("查询索引失败！");
        }
    }
}
