package com.kingwarluo.es.service.impl;

import com.google.common.collect.Lists;
import com.kingwarluo.es.domain.ShopSku;
import com.kingwarluo.es.param.SearchParam;
import com.kingwarluo.es.service.EsRestTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 参考：https://blog.csdn.net/m0_56726104/article/details/120785048
 * @author kingwarluo
 * @date 2022/5/26 14:49
 */
@Slf4j
@Service
public class EsRestTemplateServiceImpl implements EsRestTemplateService {

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Override
    public List<ShopSku> search(SearchParam param) {
        // 查询基础条件对象
        BoolQueryBuilder baseBuilder = new BoolQueryBuilder();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("skuName", param.getSkuName());
        baseBuilder.must(matchQueryBuilder);

        // 距离查询
        GeoDistanceQueryBuilder location = QueryBuilders.geoDistanceQuery("location");
        location.distance("100km").point(param.getLon(), param.getLat());
        baseBuilder.filter(location);

        // 按照距离排序
        GeoDistanceSortBuilder distanceSortBuilder = SortBuilders.geoDistanceSort("location", new GeoPoint(param.getLon(), param.getLat()))
                .unit(DistanceUnit.KILOMETERS)
                //.geoDistance(GeoDistance.ARC)  // 距离计算方式， 具体详情知识补充部分。
                .order(SortOrder.ASC);

        // 构建查询对象
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(baseBuilder)
                .withSort(distanceSortBuilder) // 根据距离正序排列
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC)) // 根据创建时间倒序排列
                .withPageable(PageRequest.of(param.getPageNo() - 1, param.getPageSize()))  // 分页查询
                .build();
        log.info("查询语句为：{}", query.getQuery().toString());
        log.info("排序语句为：{}", query.getElasticsearchSorts().toString());

        // 开始查询
        SearchHits search = restTemplate.search(query, ShopSku.class);
        List<SearchHit<ShopSku>> searchHits = search.getSearchHits();
        if (searchHits.size() == 0) {
            return Lists.newArrayList();
        }
        ArrayList<ShopSku> list = new ArrayList<>();
        searchHits.forEach((x) -> {
            ShopSku shopSku = x.getContent();
            // 此处的索引和查询返回结果中sort集合的索引一致，目的在于取返回结果中的距离计算结果，以免二次计算，造成资源浪费
            Object geoDistance = x.getSortValues().get(0);
            String distance = String.valueOf(Math.round((Double) geoDistance));
            shopSku.setDistance(distance);
            list.add(shopSku);
        });
        return list;
    }

}
