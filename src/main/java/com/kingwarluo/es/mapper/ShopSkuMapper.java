package com.kingwarluo.es.mapper;

import com.kingwarluo.es.domain.ShopSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author kingwarluo
 * @{description}
 * @date 2022/5/23 9:53
 */
@Mapper
public interface ShopSkuMapper {

    List<ShopSku> queryByKeyword(@Param("keyword") String keyword);

}
