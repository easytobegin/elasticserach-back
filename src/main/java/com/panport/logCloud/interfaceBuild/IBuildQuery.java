package com.panport.logCloud.interfaceBuild;

import com.panport.logCloud.entity.QueryParamEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by minisheep on 2019/7/25.
 * 查询条件建造类
 */
public interface IBuildQuery {
    String buildIndex(String index);
    String buildKeyword(String keyword);
    String buildPageNumber(Integer pageNumber, Integer pageSize);
    String buildPageSize(Integer pageSize);
    String buildTimeout(String timeout);
    String buildExtra(Map<String, String> map);
    String buildSource(List<String> source);
    String buildTimestampOrder(String timestamp);
    String createQuery(Map<String, Object> map);
}
