package com.kingwarluo.es.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author kingwarluo
 * @{description}
 * @date 2022/5/23 9:54
 */
@Data
@Document(indexName = "shop-sku")
public class ShopSku implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private Long shopId;

    private Long productId;

    private Long productSpecId;

    private String productSn;

    private String skuNo;

    private String spec;

    private Long productCategoryId;

    private BigDecimal discount;

    private String skuName;

    private String skuDesc;

    private Long marketPrice;

    private Long discountPrice;

    private Long retailPrice;

    private String picUrl;

    private Long createUid;

    private Long updateUid;

    private Date createTime;

    private Date updateTime;

    private Integer skuStatus;

    private Long productChannelId;

    private Integer skuType;

    private Integer isDelete;

    private Integer purchaseNumber;

    private Integer salesNumber;

    private Integer stockNumber;

    private Long hotPrice;

    private Integer vipLimit;

    private Integer shopLimit;

    private String batchNo;

    private Integer specialBannerId;

    private Integer lastShopLimit;

    private Integer isFlag;

    private Integer topFlag;

    private Date topUpdateTime;

    private Long categoryLabelId;

    private Integer lifeAuditStatus;

    private String reason;

    private Integer isSystem;

    private String categoryPName;

    private Long parentId;

    private Point location;

    /**
     * 纬度
     */
    private Double shopLat;
    /**
     * 经度
     */
    private Double shopLng;

    /**
     * 距离
     */
    private String distance;

    @Data
    public class Point {

        /**
         * 纬度
         */
        private Double lat;
        /**
         * 经度
         */
        private Double lon;
    }

}
