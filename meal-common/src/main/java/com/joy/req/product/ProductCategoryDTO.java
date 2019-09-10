package com.joy.req.product;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by SongLiang on 2019-09-06
 */
@Data
public class ProductCategoryDTO implements Serializable {

    private Integer categoryId;

    private String categoryName;

    private Integer categoryType;

    private Date createTime;

    private Date updateTime;

}
