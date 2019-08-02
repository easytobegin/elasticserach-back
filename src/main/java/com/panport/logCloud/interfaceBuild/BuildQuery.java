package com.panport.logCloud.interfaceBuild;

import com.panport.logCloud.entity.QueryParamEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by minisheep on 2019/7/25.
 */
public class BuildQuery implements IBuildQuery {

    Logger logger = LoggerFactory.getLogger(BuildQuery.class);
    private QueryParamEntity queryParamEntity;

    public BuildQuery() {
        this.queryParamEntity = new QueryParamEntity();
    }


    @Override
    public String buildIndex(String index) {
        if (index != null && !index.isEmpty()) {
            return new StringBuilder().append("/").append(index).toString();
        }
        return "";
    }

    @Override
    public String buildKeyword(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            try {
//                queryParamEntity.setKeyword(URLEncoder.encode(keyword, "UTF-8"));
                return new StringBuilder().append("/_search?pretty&q=").append(URLEncoder.encode(keyword, "UTF-8")).toString();
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }
        return "";
    }

    @Override
    public String buildPageNumber(Integer pageNumber, Integer pageSize) {
        if (pageNumber != null && pageNumber > 0) {
            int start = (pageNumber - 1) * pageSize;

//            queryParamEntity.setStart(start);
            return new StringBuilder().append("&from=").append(start).toString();
        }
        return "";
    }

    @Override
    public String buildPageSize(Integer pageSize) {
        if (pageSize != null && pageSize > 0) {
//            queryParamEntity.setPageSize(pageSize);
            return new StringBuilder().append("&size=").append(pageSize).toString();
        }
        return "";
    }

    @Override
    public String buildTimeout(String timeout) {
        if (timeout != null && !timeout.isEmpty()) {
            return new StringBuilder().append("&timeout=").append(timeout).toString();
        }
        return "";
    }

    @Override
    public String buildExtra(Map<String, String> map) {
        if (map != null && map.size() != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/_search?pretty&q=");
            // 拼接条件查询
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                String mapValue = entry.getValue();
                try {
                    stringBuilder.append(mapKey).append(":").append(URLEncoder.encode(mapValue, "UTF-8")).append("+");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            return stringBuilder.toString();
        }

        return "";
    }

    // 指定只返回哪些属性
    @Override
    public String buildSource(List<String> sources) {
        if (sources != null && sources.size() != 0) {
            StringBuilder result = new StringBuilder();
            result.append("&_source=");
            for (int i = 0; i < sources.size(); i++) {
                result.append(sources.get(i)).append(",");
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        }
        return "";
    }

    @Override
    public String buildTimestampOrder(String timestamp) {
        if (timestamp != null && !timestamp.isEmpty()) {
            return new StringBuilder().append("&sort=").append(timestamp).toString();
        }
        return "";
    }

    // 直接拼接成一个字符串回去
    // http://10.1.16.206:9200/province/_search?pretty&q=%E6%98%AF%E4%B8%AA%E5%A5%BD%E5%9C%B0%E6%96%B9
    @Override
    public String createQuery(Map<String, Object> map) {
        StringBuilder stringBuilder = new StringBuilder();
        Integer pageNumber = (Integer) map.get("pageNumber");
        Integer pageSize = (Integer) map.get("pageSize");
        String index = (String) map.get("index");
        String keyword = (String) map.get("keyword");
        String timeout = (String) map.get("timeout");
        Map<String, String> extra = (Map<String, String>) map.get("extra");
        List<String> source = (List<String>) map.get("source");
        String keywordSearch = (String) map.get("keywordSearch");
        String timestampOrderBy = (String) map.get("timestampOrder");


        //curl -X GET 'localhost:9200/get-together/group/_search?pretty&sort=created_on:asc'
        //curl -X GET 'localhost:9200/get-together/group/_search?pretty&sort=created_on:desc'

        // 只要有带精确查询参数就不用keyword
        if (extra != null && extra.size() > 0) {
            stringBuilder.append(keywordSearch).append(buildIndex(index)).append(buildExtra(extra))
                    .append(buildSource(source)).append(buildPageNumber(pageNumber, pageSize))
                    .append(buildPageSize(pageSize)).append(buildTimeout(timeout)).append(buildTimestampOrder(timestampOrderBy));

        } else {
            stringBuilder.append(keywordSearch).append(buildIndex(index)).append(buildKeyword(keyword))
                    .append(buildSource(source)).append(buildPageNumber(pageNumber, pageSize))
                    .append(buildPageSize(pageSize)).append(buildTimeout(timeout)).append(buildTimestampOrder(timestampOrderBy));
        }

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }
}
