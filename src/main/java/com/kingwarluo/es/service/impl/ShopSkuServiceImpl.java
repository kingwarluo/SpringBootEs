package com.kingwarluo.es.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kingwarluo.es.domain.ShopSku;
import com.kingwarluo.es.mapper.ShopSkuMapper;
import com.kingwarluo.es.param.SearchParam;
import com.kingwarluo.es.service.ShopSkuService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoDistance;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author kingwarluo
 * @{description}
 * @date 2022/5/23 10:02
 */
@Slf4j
@Service
public class ShopSkuServiceImpl implements ShopSkuService {

    @Autowired
    private ShopSkuMapper shopSkuMapper;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public List<ShopSku> queryByKeyword(String keyword) {
        return shopSkuMapper.queryByKeyword(keyword);
    }

    @Override
    public Boolean toEs(Integer skip) throws IOException {
        List<ShopSku> shopSkuList = queryByKeyword("");
        BulkRequest bulkRequest = new BulkRequest();

        bulkRequest.timeout("2m");

        log.info("总共迁移{}条", shopSkuList.size());
        List<ShopSku> collect = shopSkuList.stream().skip(skip).collect(Collectors.toList());
        List<List<ShopSku>> partition = Lists.partition(collect, 1000);
        Integer count = 0;
        for (List<ShopSku> shopSkus : partition) {
            log.info("从{}行开始插入", count);
            for (ShopSku shopSku : shopSkus) {
                ShopSku.Point point = new ShopSku().new Point();
                point.setLon(shopSku.getShopLng());
                point.setLat(shopSku.getShopLat());
                shopSku.setLocation(point);
                // id设置唯一值
                bulkRequest.add(new IndexRequest("shop-sku").source(JSON.toJSONString(shopSku), XContentType.JSON).id(String.valueOf(shopSku.getId())));
            }
            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            count += shopSkus.size();
            log.info("已插入{}行", count);
        }
        return true;
    }

    @Override
    public List<Map<String, Object>> search(SearchParam param) throws IOException {
        SearchRequest searchRequest = new SearchRequest("shop-sku");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(param.getPageNo());
        searchSourceBuilder.size(param.getPageSize());

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("skuName", param.getSkuName());
        boolQueryBuilder.must(matchQueryBuilder);

        GeoDistanceQueryBuilder location = QueryBuilders.geoDistanceQuery("location");
        location.distance("100km").point(param.getLon(), param.getLat());
        boolQueryBuilder.filter(location);

        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(30, TimeUnit.SECONDS));
        searchRequest.source(searchSourceBuilder);
        log.info("请求语句:{}", searchRequest.source().toString());

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<Map<String,Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            list.add(hit.getSourceAsMap());
        }
        return list;
    }

}
