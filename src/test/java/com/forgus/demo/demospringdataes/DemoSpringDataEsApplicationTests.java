package com.forgus.demo.demospringdataes;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

@SpringBootTest(classes = DemoSpringDataEsApplication.class)
@RunWith(SpringRunner.class)
@Slf4j
public class DemoSpringDataEsApplicationTests {

    @Autowired
    private ElasticsearchOperations operations;
    @Autowired
    private PlaceRepository placeRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void search() throws Exception {
        GeoPoint geoPoint = new GeoPoint(31.243453, 121.497204);
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withFilter(new GeoDistanceQueryBuilder("location").point(geoPoint).distance(3, DistanceUnit.KILOMETERS))
                .withSort(new GeoDistanceSortBuilder("location", geoPoint).unit(DistanceUnit.METERS).order(SortOrder.ASC))
                .withPageable(PageRequest.of(0,50));
        Date d1 = new Date();
        SearchHits<Place> searchHits = operations.search(queryBuilder.build(), Place.class);
        Date d2 = new Date();
        log.info("size:{},cost:{}",searchHits.getTotalHits(),d2.getTime() - d1.getTime());
        for (SearchHit<Place> searchHit : searchHits) {
            BigDecimal distance = new BigDecimal(searchHit.getSortValues().get(0).toString())
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            System.out.println(searchHit.getContent().getLocationName() + "据你" + distance + "米");
        }

    }

    @Test
    public void add() {
        List<Place> placeList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            Place place = new Place();
            place.setId(UUID.randomUUID().toString());
            place.setLocationName("name_" + place.getId());
            place.setLocation(new GeoPoint(random.nextDouble() + 31, random.nextDouble() + 121));
            placeList.add(place);
            if(placeList.size() == 50000) {
                placeRepository.saveAll(placeList);
                placeList.clear();
            }
        }
    }

}
