package com.panport.logCloud.service;

import org.elasticsearch.action.delete.DeleteResponse;

import java.util.Map;

/**
 * Created by minisheep on 2019/8/14.
 */
public interface IndexOperatorService {
    void createIndex(String index, String type, Map<String, Object> map);

    void deleteIndex(String index, String type, String id);

    void updateIndex(String index, String type, String id, Map<String, Object> map);

    String searchIndex(String index, String type, String id);
}
