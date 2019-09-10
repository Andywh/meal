package com.joy.product.converter;

import com.joy.product.model.ProductInfo;
import com.joy.resp.product.ProductInfoVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SongLiang on 2019-08-25
 */
public class ProductInfo2FoodVOConverter {

    public static ProductInfoVO convert(ProductInfo productInfo) {
        ProductInfoVO productInfoVO = new ProductInfoVO();
        BeanUtils.copyProperties(productInfo, productInfoVO);
        return productInfoVO;
    }

    public static List<ProductInfoVO> convert(List<ProductInfo> productInfoList) {
        List<ProductInfoVO> productInfoVOList = new ArrayList<>();
        for (ProductInfo productInfo : productInfoList) {
            productInfoVOList.add(convert(productInfo));
        }
        return productInfoVOList;
    }
}
