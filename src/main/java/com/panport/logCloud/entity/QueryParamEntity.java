package com.panport.logCloud.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Created by minisheep on 2019/7/25.
 * 后面考虑加入查询条件比如> < !=等
 */
@Data
public class QueryParamEntity {
    // 索引名称
    private String index;
    // 关键字查询
    private String keyword;
    // 第几页
    private Integer start;
    // 第几条
    private Integer pageSize;
    // 查询的超时时间
    private String timeout;
    // 存放字段对应的值
    private Map<String, Object> extra;
    // 执行只返回哪些属性
    private List<String> source;
}
