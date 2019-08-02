package com.panport.logCloud.repository;

import com.panport.logCloud.entity.City;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by minisheep on 2019/7/24.
 */
public interface DocumentSearchRepository extends ElasticsearchRepository<City, Long> {
}
