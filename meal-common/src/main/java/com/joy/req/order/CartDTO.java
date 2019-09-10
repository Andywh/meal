package com.joy.req.order;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by SongLiang on 2019-08-28
 */
@Data
public class CartDTO implements Serializable {

    private String productId;

    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }

}
