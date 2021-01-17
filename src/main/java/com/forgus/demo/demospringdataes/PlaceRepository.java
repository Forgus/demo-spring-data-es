package com.forgus.demo.demospringdataes;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author 孤峰
 * @since 2021/01/17
 */
public interface PlaceRepository extends ElasticsearchRepository<Place,String> {
}
