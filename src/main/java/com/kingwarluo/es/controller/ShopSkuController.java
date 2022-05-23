package com.kingwarluo.es.controller;

import com.kingwarluo.es.domain.ShopSku;
import com.kingwarluo.es.param.SearchParam;
import com.kingwarluo.es.service.ShopSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author kingwarluo
 * @{description}
 * @date 2022/5/23 10:03
 */
@RestController
public class ShopSkuController {

    @Autowired
    private ShopSkuService shopSkuService;

    @RequestMapping("/search")
    public List<Map<String, Object>> search(SearchParam param) throws IOException {
        return shopSkuService.search(param);
    }


    @RequestMapping("/toEs/{skip}")
    public Boolean toEs(@PathVariable("skip") Integer skip) throws IOException {
        return shopSkuService.toEs(skip);
    }

    @RequestMapping("/query/{keyword}")
    public List<ShopSku> queryByKeyword(@PathVariable("keyword") String keyword) {
        return shopSkuService.queryByKeyword(keyword);
    }
}
