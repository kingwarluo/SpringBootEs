package com.kingwarluo.es.config;


import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

//    @Bean
//    public RestHighLevelClient restHighLevelClient(){
//        return new RestHighLevelClient(RestClient.builder(new HttpHost("101.43.122.53",9200,"http")));
//    }

}
