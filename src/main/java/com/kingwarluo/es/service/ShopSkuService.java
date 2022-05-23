package com.kingwarluo.es.service;

import com.kingwarluo.es.domain.ShopSku;
import com.kingwarluo.es.param.SearchParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author kingwarluo
 * @{description}
 * @date 2022/5/23 10:02
 */
public interface ShopSkuService {

    List<ShopSku> queryByKeyword(String keyword);

    Boolean toEs(Integer skip) throws IOException;

    List<Map<String, Object>> search(SearchParam param) throws IOException;
}
