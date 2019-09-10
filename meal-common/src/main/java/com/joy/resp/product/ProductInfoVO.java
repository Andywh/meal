package com.joy.resp.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by SongLiang on 2019-08-25
 */
@Data
public class ProductInfoVO implements Serializable {

    @JsonProperty("id")
    String productId;

    @JsonProperty("name")
    String productName;

    @JsonProperty("price")
    BigDecimal productPrice;

    @JsonProperty("description")
    String productDescription;

    @JsonProperty("icon")
    String productIcon;

}
