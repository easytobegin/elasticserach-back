package com.panport.logCloud.serviceImpl;

import com.panport.logCloud.service.IndexOperatorService;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by minisheep on 2019/8/14.
 */
@Service
public class IndexOperatorServiceImpl implements IndexOperatorService {
    @Autowired
    private TransportClient client;



    @Override
    public void createIndex(String index, String type, Map<String, Object> map) {
        try {
            XContentBuilder xContentBuilder = jsonBuilder().startObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                xContentBuilder.field(entry.getKey(), entry.getValue());
            }
            client.prepareIndex(index, type).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE)
                    .setSource(xContentBuilder.endObject()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteIndex(String index, String type, String id) {
        client.prepareDelete(index, type, id).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).execute().actionGet();
    }

    @Override
    public void updateIndex(String index, String type, String id, Map<String, Object> map) {
        try {
            XContentBuilder xContentBuilder = jsonBuilder().startObject();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                xContentBuilder.field(entry.getKey(), entry.getValue());
            }
            client.prepareUpdate(index, type, id).setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE).setDoc(xContentBuilder.endObject()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String searchIndex(String index, String type, String id) {
        try {
            GetResponse response = client.prepareGet(index, type, id).setRefresh(true).get();
            return response.getSourceAsString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
