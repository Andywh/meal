package com.joy.resp.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SongLiang on 2019-08-25
 */
@Data
public class ProductVO implements Serializable {

    @JsonProperty("name")
    String categoryName;

    @JsonProperty("type")
    Integer categoryType;

    @JsonProperty("foods")
    List<ProductInfoVO> productInfoVOList;

}
