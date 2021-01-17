package com.forgus.demo.demospringdataes;

import lombok.Data;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author 孤峰
 * @since 2021/01/17
 */
@Data
@Document(indexName = "location_test")
public class Place {

    @Id
    private String id;
    private String locationName;
    private GeoPoint location;
}
