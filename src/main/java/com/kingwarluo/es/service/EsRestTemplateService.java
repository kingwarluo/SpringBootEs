package com.kingwarluo.es.service;

import com.kingwarluo.es.domain.ShopSku;
import com.kingwarluo.es.param.SearchParam;

import java.io.IOException;
import java.util.List;

/**
 * @author kingwarluo
 * @{description}
 * @date 2022/5/26 14:49
 */
public interface EsRestTemplateService {

    List<ShopSku> search(SearchParam param);

}
