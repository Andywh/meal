package com.joy.req.product;

import java.math.BigDecimal;

/**
 * Created by SongLiang on 2019-09-06
 */
public class ProductForm {

    private String productId;

    /** 名字. */
    private String productName;

    /** 单价. */
    private Integer productStock;

    /** 库存. */
    private BigDecimal productPrice;

    /** 描述. */
    private String productDescription;

    /** 小图. */
    private String productIcon;

    /** 状态, 0正常1下架. */
    private Integer productStatus;

    /** 类目编号. */
    private Integer categoryType;
}
