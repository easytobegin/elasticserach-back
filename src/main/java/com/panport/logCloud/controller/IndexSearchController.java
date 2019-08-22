package com.panport.logCloud.controller;

import com.alibaba.fastjson.JSONObject;
import com.panport.logCloud.entity.IndexEntity;
import com.panport.logCloud.interfaceBuild.BuildQuery;
import com.panport.logCloud.utils.HttpUtils;
import com.panport.logCloud.utils.UniUtil;
import com.panport.logCloud.utils.UrlUtil;
import com.pantech.cloud.common.msg.Message;
import com.pantech.cloud.common.msg.MessageBox;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by minisheep on 2019/7/24.
 *
 */
@RestController
public class IndexSearchController {
    @Value("${elasticsearchConfig.host}")
    private String elasticHost;

    @Value("http://" + "${elasticsearchConfig.host}" + ":" + "${elasticsearchConfig.port}")
    private String elasticHostAndPort;

    @Value("http://" + "${elasticsearchConfig.host}" + ":" + "${elasticsearchConfig.port}" + "${elasticsearchConfig.index-url}")
    private String indexUrl;

    @Value("http://" + "${elasticsearchConfig.host}" + ":" + "${elasticsearchConfig.port}" + "${elasticsearchConfig.max-size}")
    private String maxSize;

    @Value("http://" + "${elasticsearchConfig.host}" + ":" + "${elasticsearchConfig.port}" + "${elasticsearchConfig.refresh}")
    private String refreshTime;

    @Autowired
    private TransportClient client;

    // 获取index配置的详细信息
    @RequestMapping(value = "/indexInfo", method = RequestMethod.GET)
    public Message findIndexInfo(@Param(value = "index") String index) {
        String indexResult = null;
        try {
            if (index != null) {
                indexResult = HttpUtils.sendGetRequest(elasticHostAndPort + "/" + index);
            }
        } catch (IOException e) {
            return Message.fail("Http请求失败或者索引不存在！");
        }
        return new MessageBox<>(indexResult);
    }

    /*
        获取全部索引 http://10.1.16.206:9200/_cat/indices?format=json&pretty
        主要提供选择索引进行查找
     */

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public Message findAllIndex(@Param(value = "params") String params) {
        String indexResult = null;
        try {
            if (params == null) {
                indexResult = HttpUtils.sendGetRequest(indexUrl);
            } else if (params.equals("")) {
                indexResult = HttpUtils.sendGetRequest(indexUrl);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(indexUrl).append("&index=").append("*").append(params).append("*");
                indexResult = HttpUtils.sendGetRequest(stringBuilder.toString());
            }
        } catch (IOException e) {
            return Message.fail("Http请求失败或者索引不存在！");
        }
        List<IndexEntity> indexEntities = JSONObject.parseArray(indexResult, IndexEntity.class);

        return new MessageBox<>(indexEntities);
    }

    /*
        获取本索引全部字段
        http://localhost:8080/indexDetail?index=vip* *模糊匹配
     */
    @RequestMapping(value = "/indexDetail", method = RequestMethod.GET)
    public Message getIndexDetail(@Param(value = "index") String index) throws Exception {

        SearchResponse searchResponse = null;
        try {
            searchResponse = client.prepareSearch(index).get();
        } catch (Exception e) {
            searchResponse = null;
        }

        if (searchResponse != null) {
            Set<String> strings = new HashSet<>();
            SearchHits hits = searchResponse.getHits();
            for (SearchHit searchHit : hits) {
                Map<String, Object> map = searchHit.getSource();
                getTree("", map, strings);
//                strings = searchHit.getSource().keySet();
                break;
            }

            return new MessageBox<>(strings);
        } else {
            return Message.fail("找不到该索引！");
        }
    }

    private void getTree (String key, Map<String, Object> map, Set<String> strings) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry != null && entry.getValue() != null && entry.getValue() instanceof Map) {
                String temp = key;
                key += entry.getKey() + ".";
                getTree(key, (Map<String, Object>) entry.getValue(), strings);
                key = temp;
            } else {
                String temp = key;
                key += entry.getKey() + ".";
                strings.add(key.substring(0, key.length() - 1));
                key = temp;
            }
        }
    }

    /*
        根据内容模糊搜索，聚合
        需要Url编码，UTF-8格式
        sort实现
    */
    //  http://10.1.16.206:9200/vip*/_search?pretty&q=gateway
    @Value("http://" + "${elasticsearchConfig.host}" + ":" + "${elasticsearchConfig.port}")
    private String keywordSearch;

    // 参数让其拼接，选完后再传入，比如根据xxx排序，筛选字段，需要筛选的条件xxx
    @RequestMapping(value = "/keywordMatch", method = RequestMethod.POST)
    public Message getContentMatch(@RequestBody Map<String, Object> map) {
        map.put("keywordSearch", keywordSearch);

        BuildQuery buildQuery = new BuildQuery();
        String createQuery = buildQuery.createQuery(map);
        String indexResult = "";

        try {
            indexResult = HttpUtils.sendGetRequest(createQuery);
        } catch (IOException e) {
            return Message.fail("Http请求失败或者索引不存在！");
        }
        return Message.ok(indexResult);
    }


    /*
        索引的管理删除
        curl -XDELETE -u elastic:changeme http://localhost:9200/acc-apply-2018.08.09,acc-apply-2018.08.10
     */
    @RequestMapping(value = "/deleteIndex", method = RequestMethod.POST)
    public Message deleteIndex(@RequestBody Map<String, Object> map) {
        List<String> indexs = (List<String>) map.get("indexs");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(elasticHostAndPort).append("/");
        for (int i = 0; i < indexs.size(); i++) {
            stringBuilder.append(indexs.get(i)).append(",");
        }
        String url = stringBuilder.substring(0, stringBuilder.length() - 1);
        try {
            HttpUtils.delete(url);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.fail("删除索引失败！");
        }
        return Message.ok("删除索引成功！");
    }

    @RequestMapping(value = "/setMaxSize", method = RequestMethod.PUT)
    public Message setMaxSize() {
        String maxSizeResult = null;
        try {
            maxSizeResult = HttpUtils.httpPut(maxSize, "{\"max_result_window\" : 100000000}", "UTF-8");
            return Message.ok(maxSizeResult);
        } catch (Exception e) {
            return Message.fail("设置失败！");
        }
    }


    // 设置索引的刷新间隔
    @RequestMapping(value = "/refreshTime", method = RequestMethod.PUT)
    public Message modifyRefreshTime() {
        String refreshResult = null;
        try {
            refreshResult = HttpUtils.httpPut(refreshTime, "{ \"refresh_interval\": \"1s\" }", "UTF-8");
            return Message.ok(refreshResult);
        } catch (Exception e) {
            return Message.fail("设置失败！");
        }
    }
}
