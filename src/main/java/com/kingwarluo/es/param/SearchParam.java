package com.kingwarluo.es.param;

import lombok.Data;

import java.io.Serializable;

/**
 * @author kingwarluo
 * @{description}
 * @date 2022/5/23 15:03
 */
@Data
public class SearchParam implements Serializable {

    private String skuName;

    private Double lat;

    private Double lon;

    private Integer pageNo;

    private Integer pageSize;

}
